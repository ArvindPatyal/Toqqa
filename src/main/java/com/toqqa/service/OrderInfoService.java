package com.toqqa.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.domain.OrderInfo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.ToggleOrdersStatus;

public interface OrderInfoService {

	Response<?> placeOrder(OrderPayload orderPayload);

	OrderInfoBo fetchOrderInfo(String id);

	ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationbo);

//	String orderInvoice(String id);

	Response<?> updateOrder(String orderId);

	List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo);

	OrderInfoBo orderDetails(String orderId);

	ListResponseWithCount<OrderInfoBo> list(ToggleOrdersStatus toggleOrdersStatus);

	Response updateOrderStatus(OrderStatusUpdatePayload orderStatusUpdatePayload);
	
	Optional<Integer> getDeliveredOrderCountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate);
	
	Optional<Integer> getOrderCountBySmeAndDateAndStatus(String smeId, String orderStatus, LocalDate startDate, LocalDate endDate);


}