package com.toqqa.service.impls;


import com.toqqa.bo.*;
import com.toqqa.constants.OrderStatus;
import com.toqqa.constants.PaymentConstants;
import com.toqqa.domain.*;
import com.toqqa.dto.EmailRequestDto;
import com.toqqa.dto.OrderInfoDto;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.*;
import com.toqqa.repository.*;
import com.toqqa.service.*;
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private OrderInfoRepository orderInfoRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private DeliveryAddressRepository addressRepo;
    @Autowired
    private AuthenticationService authenticationService;
    @Value("${pageSize}")
    private Integer pageSize;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SmeRepository smeRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Autowired
    private SellerRatingRepository sellerRatingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Helper helper;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private SmeService smeService;


    @Override
    public Response<?> placeOrder(OrderPayload orderPayload) {
        log.info("Invoked :: OrderInfoServiceImpl :: placeOrder()");
        User user = this.authenticationService.currentUser();
        Set<String> sellerIds = new HashSet<>();
        orderPayload.getItems().forEach(orderItemPayload -> sellerIds.add(orderItemPayload.getSellerUserId()));
        Optional<DeliveryAddress> optionalAddress = this.addressRepo.findById(orderPayload.getAddressId());
        if (optionalAddress.isPresent()) {
            DeliveryAddress address = optionalAddress.get();

            sellerIds.forEach(s -> {
                List<OrderItemPayload> orderItems = orderPayload.getItems().stream()
                        .filter(orderItemPayload -> orderItemPayload.getSellerUserId().equals(s))
                        .collect(Collectors.toList());

                AtomicReference<Double> orderAmount = new AtomicReference<>(0.0);
                AtomicReference<Double> shippingFee = new AtomicReference<>(0.0);

                orderItems.forEach(orderItemPayload -> {
                    shippingFee.set(orderItemPayload.getShippingFee());

                });
                Sme sme = this.smeRepository.findByUserId(s);
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setSme(sme);
                orderInfo.setEmail(orderPayload.getEmail());
                orderInfo.setPhone(orderPayload.getPhone());
                orderInfo.setFirstName(orderPayload.getFirstName());
                orderInfo.setLastName(orderPayload.getLastName());
                orderInfo.setShippingFee(shippingFee.get());
                orderInfo.setAddress(address);
                orderInfo.setUser(user);
                String random = RandomString.make(7).toUpperCase();
                orderInfo.setInvoiceNumber(Constants.INVOICE_CONSTANT + random);
                orderInfo.setOrderTransactionId(Constants.ORDER_CONSTANT + random);
                orderInfo.setOrderStatus(OrderStatus.PLACED);
                orderInfo.setPaymentType(PaymentConstants.CASH_ON_DELIVERY);
                orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);
                List<OrderItem> orderItemList = this.persistOrderItems(orderItems, orderInfo);
                orderInfo.setOrderItems(orderItemList);
                orderItemList.forEach(orderItem -> orderAmount.set(orderAmount.get() + orderItem.getPrice()));
                orderInfo.setAmount(orderAmount.get());
                this.orderInfoRepo.saveAndFlush(orderInfo);
                SmeBo smeBo = new SmeBo(sme);
                OrderInfoBo bo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);
                this.invoiceService.generateInvoice(bo, user);

                if (orderInfo.getEmail() != null) {
                    EmailRequestDto emailRequestDto = new EmailRequestDto();
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("data", orderInfo);
                    emailRequestDto.setData(dataMap);
                    emailRequestDto.setOrder(true);
                    this.emailService.sendEmail(emailRequestDto);
                }
                pushNotificationService.sendNotificationToSmeForOrder(bo);
                pushNotificationService.sendNotificationToSmeForProduct(bo);
            });
        } else {
            throw new BadRequestException("Invalid address id");
        }
        this.cartRepository.deleteByUser(user);
        return new Response<>("true", "order placed successfully");
    }

    @Override
    public Response<?> cancelOrder(OrderCancelPayload cancelPayload) {
        log.info("Invoked :: OrderInfoServiceImpl :: cancelOrder()");
        Optional<OrderInfo> optionalOrderInfo = this.orderInfoRepo.findById(cancelPayload.getOrderId());
        if (optionalOrderInfo.isPresent()) {
            OrderInfo orderInfo = optionalOrderInfo.get();
            if (orderInfo.getOrderStatus().ordinal() >= OrderStatus.READY_FOR_DISPATCH.ordinal()) {
                throw new BadRequestException("Order cannot be cancelled");
            }
            orderInfo.setOrderStatus(OrderStatus.CANCELLED);
            orderInfo.setCancellationReason(cancelPayload.getCancellationReason());

            orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);

            orderInfo.getOrderItems().forEach(orderItem -> {
                Product product = orderItem.getProduct();
                product.setUnitsInStock(product.getUnitsInStock() + orderItem.getQuantity());

                this.productRepo.saveAndFlush(product);
            });
            this.pushNotificationService.orderCancelToSme(this.userService.getById(orderInfo.getSme().getUserId()));
            return new Response<>(true, "Order cancelled successfully ");
        } else {
            throw new BadRequestException(
                    "Order not found with id" + cancelPayload.getOrderId() + " Enter a valid orderId");
        }

    }

    private List<OrderItem> persistOrderItems(List<OrderItemPayload> orderItems, OrderInfo order) {
        log.info("Invoked :: OrderInfoServiceImpl :: persistOrderItems()");
        List<OrderItem> orderItemsList = new ArrayList<OrderItem>();
        List<Product> products = new ArrayList<>();
        for (OrderItemPayload item : orderItems) {
            OrderItem orderItem = new OrderItem();
            Optional<Product> optionalProduct = this.productRepo.findById(item.getProductId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                orderItem.setDiscount(product.getDiscount());
                orderItem.setPricePerUnit(product.getPricePerUnit());
                orderItem.setProduct(product);
                if (item.getQuantity() > product.getMaximumUnitsInOneOrder()) {
                    throw new BadRequestException("Maximum quantity in" + " " + product.getProductName() + " " + product.getMaximumUnitsInOneOrder());
                }
                if (item.getQuantity() < product.getMinimumUnitsInOneOrder()) {
                    throw new BadRequestException("Minimum quantity in" + " " + product.getProductName() + " " + product.getMinimumUnitsInOneOrder());
                }
                orderItem.setQuantity(item.getQuantity());
                orderItem.setOrderInfo(order);
                orderItem.setPrice((product.getPricePerUnit() * item.getQuantity())
                        - (product.getPricePerUnit() * product.getDiscount() * orderItem.getQuantity()) / 100);
                orderItem = this.orderItemRepo.saveAndFlush(orderItem);
                orderItemsList.add(orderItem);
                if (orderItem.getQuantity() <= product.getUnitsInStock()) {
                    product.setUnitsInStock(product.getUnitsInStock() - item.getQuantity());
                    products.add(product);
                } else {
                    throw new BadRequestException("order quantity exceeds maximum unit with this product::"
                            + product.getProductName() + "::Quantity left in Stock::" + product.getUnitsInStock());
                }
            } else {
                throw new BadRequestException("invalid product id " + item.getProductId());
            }
            this.productRepo.saveAllAndFlush(products);
        }
        return orderItemsList;
    }

    @Override
    public List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo) {
        log.info("Invoked :: OrderInfoServiceImpl :: fetchOrderItems()");

        User user = this.authenticationService.currentUser();
        List<OrderItem> orderItems = this.orderItemRepo.findByOrderInfo(orderInfo);
        List<OrderItemBo> orderItemBos = new ArrayList<OrderItemBo>();
        orderItems.forEach(orderItem -> {
            ProductBo productBo = this.productService.toProductBo(orderItem.getProduct());
            productBo.setPricePerUnit(orderItem.getPricePerUnit());
            productBo.setDiscount(orderItem.getDiscount());
            if (orderInfo.getOrderStatus() == OrderStatus.DELIVERED) {
                ProductRating productRating = this.productRatingRepository.findByProductIdAndUser(orderItem.getProduct().getId(), user);
                if (productRating != null) {
                    productBo.setProductRatingBo(new ProductRatingBo(productRating));
                }
            }
            OrderItemBo orderItemBo = new OrderItemBo(orderItem, productBo);
            orderItemBos.add(orderItemBo);
        });
        return orderItemBos;
    }

    @Override
    public OrderInfoBo fetchOrderInfo(String id) {
        log.info("Invoked :: OrderInfoServiceImpl :: fetchOrderInfo()");
        Optional<OrderInfo> orderInfo = this.orderInfoRepo.findById(id);
        User user = this.authenticationService.currentUser();
        if (orderInfo.isPresent()) {
            Sme sme = orderInfo.get().getSme();
            SmeBo smeBo = new SmeBo(sme);
            if (orderInfo.get().getOrderStatus() == OrderStatus.DELIVERED) {
                SellerRating sellerRating = this.sellerRatingRepository.findBySmeIdAndUser_Id(smeBo.getId(), user.getId());
                if (sellerRating != null) {
                    smeBo.setSellerRatingBo(new SellerRatingBo(sellerRating));
                }
            }
            OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo.get(), this.fetchOrderItems(orderInfo.get()), smeBo);
            orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(id, orderInfo.get().getUser().getId()));
            return orderInfoBo;
        }
        throw new BadRequestException("no order found with id= " + id + " Enter a valid order Id");

    }

    @Override
    public ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationBo) {
        log.info("Invoked :: OrderInfoServiceImpl :: fetchOrderList()");
        User user = this.authenticationService.currentUser();
        Sort sort = Sort.by("createdDate").descending();
        Page<OrderInfo> allOrders = this.orderInfoRepo.findByUser(PageRequest.of(paginationBo.getPageNumber(), pageSize, sort), user);
        List<OrderInfoBo> bos = new ArrayList<OrderInfoBo>();
        allOrders.forEach(orderInfo -> {
            Sme sme = orderInfo.getSme();
            SmeBo smeBo = new SmeBo(sme);

            smeBo.setBusinessLogo(this.helper.prepareResource(sme.getBusinessLogo()));
            if (orderInfo.getOrderStatus() == OrderStatus.DELIVERED) {
                SellerRating sellerRating = this.sellerRatingRepository.findBySmeIdAndUser_Id(smeBo.getId(), user.getId());
                if (sellerRating != null) {
                    smeBo.setSellerRatingBo(new SellerRatingBo(sellerRating));
                }
            }
            OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);
            orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(orderInfo.getId(), user.getId()));
            bos.add(orderInfoBo);
        });
        return new ListResponseWithCount<OrderInfoBo>(bos, " ", allOrders.getTotalElements(),
                paginationBo.getPageNumber(), allOrders.getTotalPages());
    }

    @Override
    public ListResponseWithCount<OrderInfoBo> list(ToggleOrdersStatus toggleOrdersStatus) {
        log.info("Invoked :: OrderInfoServiceImpl :: list()");
        User user = this.authenticationService.currentUser();
        String userId = user.getId();
        Sme sme = this.smeRepository.findByUserId(userId);
        if (sme == null) {
            throw new BadRequestException("You Are not an sme");
        }
        String smeId = sme.getId();
        Page<OrderInfo> orderInfo = null;
        if (toggleOrdersStatus.getOrderStatus() != null) {
            Sort sort = Sort.by("modificationDate").descending();
            orderInfo = this.orderInfoRepo.findBySmeIdAndOrderStatusIn(
                    PageRequest.of(toggleOrdersStatus.getPageNumber(), pageSize, sort), smeId,
                    toggleOrdersStatus.getOrderStatus());
        } else {
            throw new BadRequestException("invalid OrderStatus or empty");
        }
        List<OrderInfoBo> list = new ArrayList<>();
        orderInfo.forEach(order -> {
            String userid = order.getUser().getId();
            SmeBo smeBo = new SmeBo(order.getSme());
            OrderInfoBo orderBo = new OrderInfoBo(order, this.fetchOrderItems(order), smeBo);
            orderBo.setInvoiceUrl(this.invoiceService.fetchInvoice(orderBo.getId(), userid));
            list.add(orderBo);
        });
        return new ListResponseWithCount<OrderInfoBo>(list, " ", orderInfo.getTotalElements(),
                toggleOrdersStatus.getPageNumber(), orderInfo.getTotalPages());

    }

    @Override
    public OrderInfoBo orderDetails(String orderId) {
        log.info("Invoked :: OrderInfoServiceImpl :: orderDetails()");
        Optional<OrderInfo> optionalOrderInfo = this.orderInfoRepo.findById(orderId);
        if (optionalOrderInfo.isPresent()) {
            User user = this.authenticationService.currentUser();
            OrderInfo orderInfo = optionalOrderInfo.get();
            if (user.getId().equals(orderInfo.getSme().getUserId())) {
                Sme sme = orderInfo.getSme();
                SmeBo smeBo = new SmeBo(sme);
                OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);
                orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(orderId, orderInfo.getUser().getId()));
                return orderInfoBo;
            } else {
                throw new BadRequestException("Not a valid Sme");
            }
        } else {
            throw new BadRequestException("Enter a valid order Id");
        }
    }

    @Override
    public Response<?> updateOrderStatus(OrderStatusUpdatePayload orderStatusUpdatePayload) {
        log.info("Invoked :: OrderInfoServiceImpl :: updateOrderStatus()");
        Optional<OrderInfo> optionalOrderInfo = this.orderInfoRepo.findById(orderStatusUpdatePayload.getOrderId());
        if (optionalOrderInfo.isPresent()) {
            OrderInfo orderInfo = optionalOrderInfo.get();
            User user = this.authenticationService.currentUser();
            Sme sme = this.smeRepository.findByUserId(user.getId());
            if (sme != null && sme.getId() == orderInfo.getSme().getId()) {
                if (orderInfo.getOrderStatus().ordinal() + 1 == (orderStatusUpdatePayload.getOrderStatus().ordinal())) {
                    orderInfo.setOrderStatus((orderStatusUpdatePayload.getOrderStatus()));
                    this.orderInfoRepo.saveAndFlush(orderInfo);
                    pushNotificationService.sendNotificationToCustomer(orderStatusUpdatePayload, orderInfo.getUser());
                    if (orderInfo.getOrderStatus() == OrderStatus.DELIVERED) {
                        this.pushNotificationService.sendNotificationToCustomerForRating(orderInfo.getUser());
                    }
                    return new Response<>("", "ORDER STATUS UPDATED SUCCESSFULLY");
                } else {
                    throw new BadRequestException("Cannot update orderStatus");
                }
            } else {
                throw new BadRequestException("You are not an SME Or associated with this order");
            }
        } else {
            throw new ResourceNotFoundException("Enter a valid order Id");
        }

    }

    @Override
    public Optional<Integer> getDeliveredOrderCountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate) {
        return orderInfoRepo.findDelOrderAmountBySmeAndDate(smeId, startDate, endDate);

    }

    @Override
    public Optional<Integer> getOrderCountBySmeAndDateAndStatus(String smeId, String orderStatus, LocalDate startDate, LocalDate endDate) {
        return orderInfoRepo.findOrderCountBySmeAndDateAndStatus(smeId, orderStatus, startDate, endDate);
    }


    //TODO to be discussed further!!
    @Override
    public ListResponseWithCount previousOrderList(OrderInfoDto orderInfoDto) {
        User user = this.authenticationService.currentUser();
        Page<OrderInfo> orderInfoList = null;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        if (orderInfoDto.isShowCancelledOrders()) {
            orderInfoList = this.orderInfoRepo.findByCreatedDateBetweenAndUserIdAndOrderStatusIn(PageRequest.of(orderInfoDto.getPageNumber(), pageSize, sort),
                    orderInfoDto.getStartDate(), orderInfoDto.getEndDate(), user.getId(), Collections.singletonList(OrderStatus.CANCELLED));
        } else {
            orderInfoList = this.orderInfoRepo.findByCreatedDateBetweenAndUserIdAndOrderStatusIn(PageRequest.of(orderInfoDto.getPageNumber(), pageSize, sort),
                    orderInfoDto.getStartDate(), orderInfoDto.getEndDate(), user.getId(), Constants.ORDER_STATUSES);
        }
        List<OrderInfoBo> orderInfoBos = new ArrayList<>();
        orderInfoList.forEach(orderInfo -> {
            SmeBo smeBo = this.smeService.toSmeBo(orderInfo.getSme());
            smeBo.setBusinessLogo(this.helper.prepareResource(smeBo.getBusinessLogo()));
            if (orderInfo.getOrderStatus() == OrderStatus.DELIVERED) {
                SellerRating sellerRating = this.sellerRatingRepository.findBySmeIdAndUser_Id(smeBo.getId(), user.getId());
                if (sellerRating != null) {
                    smeBo.setSellerRatingBo(new SellerRatingBo(sellerRating));
                }
            }
            OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);
            orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(orderInfo.getId(), user.getId()));
            orderInfoBos.add(orderInfoBo);
        });
        return new ListResponseWithCount(orderInfoBos, "User previous OrderList ", orderInfoList.getTotalElements(), orderInfoDto.getPageNumber(), orderInfoList.getTotalPages());
    }
}
