package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderPayload;

public interface OrderInfoService {

	OrderInfoBo placeOrder(OrderPayload orderPayload);

	OrderInfoBo fetchOrderInfo(String id);

	ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationbo);

	void orderInvoice(String id);

}