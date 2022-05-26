package com.toqqa.service.impls;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.OrderConstants;
import com.toqqa.constants.PaymentConstants;
import com.toqqa.domain.*;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderItemPayload;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.Response;
import com.toqqa.repository.*;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.EmailService;
import com.toqqa.service.InvoiceService;
import com.toqqa.service.OrderInfoService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private Helper helper;
    @Value("${pageSize}")
    private Integer pageSize;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SmeRepository smeRepository;

//    @Override
//    public OrderInfoBo placeOrder(OrderPayload orderPayload) {
//        log.info("Inside Add OrderInfo");
//        User user = this.authenticationService.currentUser();
//        OrderInfo orderInfo = new OrderInfo();
//        orderInfo.setAmount(orderPayload.getAmount());
//        orderInfo.setEmail(orderPayload.getEmail());
//        orderInfo.setPhone(orderPayload.getPhone());
//        orderInfo.setFirstName(orderPayload.getFirstName());
//        orderInfo.setLastName(orderPayload.getLastName());
//        orderInfo.setAddress(this.addressRepo.findById(orderPayload.getAddressId()).get());
//        orderInfo.setUser(user);
//        orderInfo.setOrderStatus(OrderConstants.ORDER_PLACED.getValue());
//        orderInfo.setPaymentType(PaymentConstants.CASH_ON_DELIVERY);
//        orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);
//        orderInfo.setOrderItems(this.persistOrderItems(orderPayload.getItems(), orderInfo));
//        OrderInfoBo bo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo));
//
//        this.cartRepository.deleteByUser(user);
//        this.invoiceService.generateInvoice(bo, user);
//        if (orderInfo.getEmail() != null) {
//            this.emailService.sendOrderEmail(orderInfo);
//        } else {
//            // TODO handle else case in future
//        }
//        return bo;
//    }
//

    @Override
    public Response placeOrder(OrderPayload orderPayload) {
        User user = this.authenticationService.currentUser();
        Set<String> sellerIds = new HashSet<>();
        orderPayload.getItems().forEach(orderItemPayload -> sellerIds.add(orderItemPayload.getSellerUserId()));
        DeliveryAddress address = this.addressRepo.findById(orderPayload.getAddressId()).get();
        sellerIds.forEach(s -> {
            List<OrderItemPayload> orderItems = orderPayload.getItems().stream().filter(orderItemPayload -> orderItemPayload.getSellerUserId().equals(s)).collect(Collectors.toList());
            AtomicReference<Double> orderAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> shippingFee = new AtomicReference<>(0.0);
            orderItems.forEach(orderItemPayload -> {
                orderAmount.set(orderAmount.get() + (orderItemPayload.getPrice() * orderItemPayload.getQuantity()));
                shippingFee.set(orderItemPayload.getShippingFee());
            });
            Sme sme = this.smeRepository.findByUserId(s);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setSme(sme);
            orderInfo.setAmount(orderAmount.get());
            orderInfo.setEmail(orderPayload.getEmail());
            orderInfo.setPhone(orderPayload.getPhone());
            orderInfo.setFirstName(orderPayload.getFirstName());
            orderInfo.setLastName(orderPayload.getLastName());
            orderInfo.setShippingFee(shippingFee.get());
            orderInfo.setAddress(address);
            orderInfo.setUser(user);
            orderInfo.setOrderStatus(OrderConstants.ORDER_PLACED.getValue());
            orderInfo.setPaymentType(PaymentConstants.CASH_ON_DELIVERY);
            orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);
            orderInfo.setOrderItems(this.persistOrderItems(orderItems, orderInfo));
            SmeBo smeBo = new SmeBo(sme);
            OrderInfoBo bo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);

//            bo.setSmeBo(smeBo);


            this.invoiceService.generateInvoice(bo, user);
        });
        this.cartRepository.deleteByUser(user);
        return new Response<>("true", "order placed successfully");
    }

    private List<OrderItem> persistOrderItems(List<OrderItemPayload> orderItems, OrderInfo order) {
        log.info("persist OrderItems");
        List<OrderItem> oItems = new ArrayList<OrderItem>();
        for (OrderItemPayload item : orderItems) {
            OrderItem parent = new OrderItem();

            parent.setPrice(item.getPrice());
            if (!this.helper.notNullAndBlank(item.getProductId())) {
                throw new BadRequestException("invalid product id " + item.getProductId());
            }
            Product product = this.productRepo.findById(item.getProductId()).get();
//            parent.setDiscount(product.getDiscount());
            parent.setProduct(product);
            parent.setQuantity(item.getQuantity());
            parent.setOrderInfo(order);
            parent = this.orderItemRepo.saveAndFlush(parent);
            oItems.add(parent);
        }
        return oItems;
    }

    private List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo) {
        log.info("fetch OrderItems");
        List<OrderItem> items = this.orderItemRepo.findByOrderInfo(orderInfo);
        List<OrderItemBo> itemBos = new ArrayList<OrderItemBo>();
        items.forEach(item -> {
            itemBos.add(new OrderItemBo(item));
        });
        return itemBos;
    }

    @Override
    public OrderInfoBo fetchOrderInfo(String id) {
        log.info("Inside fetch order");
        Optional<OrderInfo> orderInfo = this.orderInfoRepo.findById(id);
        if (orderInfo.isPresent()) {
            Sme sme = orderInfo.get().getSme();
            SmeBo smeBo = new SmeBo(sme);
            OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo.get(), this.fetchOrderItems(orderInfo.get()), smeBo);
            orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(id, orderInfo.get().getUser().getId()));
            return orderInfoBo;
        }
        throw new BadRequestException("no order found with id= " + id);

    }

    @Override
    public ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationBo) {
        User user = this.authenticationService.currentUser();
        Page<OrderInfo> allOrders = null;
        if (authenticationService.isAdmin()) {
            allOrders = this.orderInfoRepo.findAll(PageRequest.of(paginationBo.getPageNumber(), pageSize));
        } else {
            allOrders = this.orderInfoRepo.findByUser(PageRequest.of(paginationBo.getPageNumber(), pageSize), user);
        }
        List<OrderInfoBo> bos = new ArrayList<OrderInfoBo>();
        allOrders.forEach(orderInfo ->
        {
            Sme sme = orderInfo.getSme();
            SmeBo smeBo = new SmeBo(sme);
            OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);
            orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(orderInfo.getId(), user.getId()));
            bos.add(orderInfoBo);
        });
        return new ListResponseWithCount<OrderInfoBo>(bos, "", allOrders.getTotalElements(),
                paginationBo.getPageNumber(), allOrders.getTotalPages());
    }

    @Override
    public String orderInvoice(String id) {
        String userId = this.authenticationService.currentUser().getId();
        return this.invoiceService.fetchInvoice(id, userId);
    }
}
