package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.Response;

public interface OrderInfoService {

	OrderInfoBo placeOrder(OrderPayload orderPayload);

	OrderInfoBo fetchOrderInfo(String id);

	ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationbo);

	Response orderInvoice(String id);

}