package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.OrderConstants;
import com.toqqa.constants.PaymentConstants;
import com.toqqa.domain.DeliveryAddress;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.OrderItem;
import com.toqqa.domain.Product;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderItemPayload;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.payload.OrderUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.ToggleOrdersStatus;
import com.toqqa.repository.CartRepository;
import com.toqqa.repository.DeliveryAddressRepository;
import com.toqqa.repository.OrderInfoRepository;
import com.toqqa.repository.OrderItemRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.InvoiceService;
import com.toqqa.service.OrderInfoService;
import com.toqqa.service.ProductService;

import lombok.extern.slf4j.Slf4j;

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

	@Override
	public Response<?> placeOrder(OrderPayload orderPayload) {
		log.info("Inside Service placeOrder");
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
				orderInfo.setOrderStatus(OrderConstants.PLACED);
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
				/*
				 * if (orderInfo.getEmail() != null) {
				 * this.emailService.sendOrderEmail(orderInfo); }
				 */
			});
		} else {
			throw new ResourceNotFoundException("Invalid address id");
		}
		this.cartRepository.deleteByUser(user);
		return new Response<>("true", "order placed successfully");
	}

	@Override
	public Response<?> updateOrder(OrderUpdatePayload orderUpdatePayload) {
		log.info("Inside Service UpdateOrder");
		User user = this.authenticationService.currentUser();
		OrderInfo orderInfo = this.orderInfoRepo.findByIdAndUser(orderUpdatePayload.getOrderId(), user);
		if (orderInfo != null) {
			if (orderUpdatePayload.getIsCancelled() == true) {
				orderInfo.setOrderStatus(OrderConstants.CANCELED);
				orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);

				return new Response<>(true, "Order cancelled successfully ");
			} else {
				return new Response<>(false, "Order status not updated ");
			}

		} else {

			throw new ResourceNotFoundException("Enter valid orderId for this user");
		}
	}

	private List<OrderItem> persistOrderItems(List<OrderItemPayload> orderItems, OrderInfo order) {
		log.info("persist OrderItems");
		List<OrderItem> orderItemsList = new ArrayList<OrderItem>();
		for (OrderItemPayload item : orderItems) {
			OrderItem orderItem = new OrderItem();
			Optional<Product> optionalProduct = this.productRepo.findById(item.getProductId());
			if (optionalProduct.isPresent()) {
				Product product = optionalProduct.get();
				orderItem.setDiscount(product.getDiscount());
				orderItem.setPricePerUnit(product.getPricePerUnit());
				orderItem.setProduct(product);
				orderItem.setQuantity(item.getQuantity());
				orderItem.setOrderInfo(order);
				orderItem.setPrice((product.getPricePerUnit() * item.getQuantity())
						- (product.getPricePerUnit() * product.getDiscount() * orderItem.getQuantity()) / 100);
				orderItem = this.orderItemRepo.saveAndFlush(orderItem);
				orderItemsList.add(orderItem);
			} else {
				throw new ResourceNotFoundException("invalid product id " + item.getProductId());
			}
		}
		return orderItemsList;
	}

	@Override
	public List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo) {
		log.info("fetch OrderItems");

		List<OrderItem> orderItems = this.orderItemRepo.findByOrderInfo(orderInfo);
		List<OrderItemBo> orderItemBos = new ArrayList<OrderItemBo>();
		orderItems.forEach(orderItem -> {
			ProductBo productBo = this.productService.toProductBo(orderItem.getProduct());
			productBo.setPricePerUnit(orderItem.getPricePerUnit());
			productBo.setDiscount(orderItem.getDiscount());
			OrderItemBo orderItemBo = new OrderItemBo(orderItem, productBo);
			orderItemBos.add(orderItemBo);
		});
		return orderItemBos;
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
		throw new ResourceNotFoundException("no order found with id= " + id);

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
		allOrders.forEach(orderInfo -> {
			Sme sme = orderInfo.getSme();
			SmeBo smeBo = new SmeBo(sme);
			OrderInfoBo orderInfoBo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo), smeBo);
			orderInfoBo.setInvoiceUrl(this.invoiceService.fetchInvoice(orderInfo.getId(), user.getId()));
			bos.add(orderInfoBo);
		});
		return new ListResponseWithCount<OrderInfoBo>(bos, " ", allOrders.getTotalElements(),
				paginationBo.getPageNumber(), allOrders.getTotalPages());
	}

	@Override
	public String orderInvoice(String id) {
		String userId = this.authenticationService.currentUser().getId();
		return this.invoiceService.fetchInvoice(id, userId);
	}

	@Override
	public ListResponseWithCount<OrderInfoBo> list(ToggleOrdersStatus toggleOrdersStatus) {
		log.info("Orders list of Customer (Live & Cancelled)");
		User user = this.authenticationService.currentUser();
		String userId = user.getId();
		Sme sme = this.smeRepository.findByUserId(userId);
		String smeId = sme.getId();
		Page<OrderInfo> orderInfo = null;
		if (toggleOrdersStatus.getOrderStatus() != null) {
			orderInfo = this.orderInfoRepo.findBySmeIdAndOrderStatusIn(
					PageRequest.of(toggleOrdersStatus.getPageNumber(), pageSize), smeId,
					toggleOrdersStatus.getOrderStatus());
		} else {
			throw new ResourceNotFoundException("OrderStatus empty");
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
		log.info("Inside Sme Service orderDetails");
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
				throw new ResourceNotFoundException("Not a valid Sme");
			}
		} else {
			throw new ResourceNotFoundException("Enter a valid order Id");
		}
	}

	@Override
	public Response updateOrderStatus(OrderStatusUpdatePayload orderStatusUpdatePayload) {
		log.info("Inside sme Service updateOrderStatus");
		Optional<OrderInfo> optionalOrderInfo = this.orderInfoRepo.findById(orderStatusUpdatePayload.getOrderId());
		try {
			orderStatusUpdatePayload.getOrderConstant();
		} catch (Exception e) {
			throw new ResourceNotFoundException(
					"invalid order constant " + orderStatusUpdatePayload.getOrderConstant());
		}

		if (optionalOrderInfo.isPresent()) {
			OrderInfo orderInfo = optionalOrderInfo.get();
			if ((orderStatusUpdatePayload.getOrderConstant()).ordinal() <= orderInfo.getOrderStatus().ordinal()) {
				throw new ResourceNotFoundException("Cannot reverse orderStatus");

			} else {
				orderInfo.setOrderStatus((orderStatusUpdatePayload.getOrderConstant()));
				this.orderInfoRepo.saveAndFlush(orderInfo);
				return new Response("", "ORDER STATUS UPDATED SUCCESSFULLY");
			}
		} else {
			throw new ResourceNotFoundException("Enter a valid order Id");
		}

	}

}
