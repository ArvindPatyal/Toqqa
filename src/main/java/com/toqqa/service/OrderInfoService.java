package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.domain.OrderInfo;
import com.toqqa.payload.*;

import java.util.List;

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

}