package com.toqqa.service;

import java.util.List;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.domain.OrderInfo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.payload.OrderUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.ToggleOrdersStatus;

public interface OrderInfoService {

	Response<?> placeOrder(OrderPayload orderPayload);

	Response<?> updateOrder(OrderUpdatePayload orderUpdatePayload);

	OrderInfoBo fetchOrderInfo(String id);

	ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationbo);

	String orderInvoice(String id);

	List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo);

	OrderInfoBo orderDetails(String orderId);

	ListResponseWithCount<OrderInfoBo> list(ToggleOrdersStatus toggleOrdersStatus);

	Response updateOrderStatus(OrderStatusUpdatePayload orderStatusUpdatePayload);

}