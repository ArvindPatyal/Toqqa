package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.toqqa.constants.OrderConstants;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.OrderItem;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderItemPayload;
import com.toqqa.payload.OrderPayload;
import com.toqqa.repository.OrderInfoRepository;
import com.toqqa.repository.OrderItemRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.OrderInfoService;
import com.toqqa.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

	@Autowired
	private OrderItemRepository orderItemRepo;

	@Autowired
	private OrderInfoRepository orderInfoRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private Helper helper;

	@Value("${pageSize}")
	private Integer pageSize;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OrderInfoBo placeOrder(OrderPayload orderPayload) {
		log.info("Inside Add OrderInfo");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setAmount(orderPayload.getAmount());
		orderInfo.setCreatedDate(new Date());
		orderInfo.setUser(this.authenticationService.currentUser());
		orderInfo.setOrderStatus(OrderConstants.ORDER_PLACED.getValue());

		orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);
		orderInfo.setOrderItems(this.persistOrderItems(orderPayload.getItems(), orderInfo));
		return new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private List<OrderItem> persistOrderItems(List<OrderItemPayload> orderItems, OrderInfo order) {
		log.info("persist OrderItems");
		List<OrderItem> oItems = new ArrayList<OrderItem>();
		for (OrderItemPayload item : orderItems) {
			OrderItem parent = new OrderItem();

			parent.setPrice(item.getPrice());
			if (!this.helper.notNullAndBlank(item.getProductId())) {
				throw new BadRequestException("invalid product id " + item.getProductId());
			}
			parent.setProduct(this.productRepo.findById(item.getProductId()).get());
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
			return new OrderInfoBo(orderInfo.get(), this.fetchOrderItems(orderInfo.get()));
		}
		throw new BadRequestException("no order found with id= " + id);

	}

	@Override
	public ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationBo) {
		User user = this.authenticationService.currentUser();
		Page<OrderInfo> allOrders = null;
		if (paginationBo.getByUserflag()) {
			allOrders = this.orderInfoRepo.findByUser(PageRequest.of(paginationBo.getPageNumber(), pageSize), user);
		} else {
			allOrders = this.orderInfoRepo.findAll(PageRequest.of(paginationBo.getPageNumber(), pageSize));
		}
		List<OrderInfoBo> bos = new ArrayList<OrderInfoBo>();
		allOrders.forEach(orderInfo -> bos.add(new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo))));
		return new ListResponseWithCount<OrderInfoBo>(bos, "", allOrders.getTotalElements(),
				paginationBo.getPageNumber(), allOrders.getTotalPages());
	}

}
