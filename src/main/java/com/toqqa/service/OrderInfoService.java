package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.domain.OrderInfo;
import com.toqqa.payload.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderInfoService {

    Response<?> placeOrder(OrderPayload orderPayload);

    OrderInfoBo fetchOrderInfo(String id);

    ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationbo);

//	String orderInvoice(String id);

    Response<?> cancelOrder(OrderCancelPayload cancelPayload);

    List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo);

    OrderInfoBo orderDetails(String orderId);

    ListResponseWithCount<OrderInfoBo> list(ToggleOrdersStatus toggleOrdersStatus);

    Response updateOrderStatus(OrderStatusUpdatePayload orderStatusUpdatePayload);

    Optional<Integer> getDeliveredOrderCountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate);

    Optional<Integer> getOrderCountBySmeAndDateAndStatus(String smeId, String orderStatus, LocalDate startDate, LocalDate endDate);


}