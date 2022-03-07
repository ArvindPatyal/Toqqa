package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.payload.OrderPayload;

public interface OrderInfoService {

	OrderInfoBo placeOrder(OrderPayload orderPayload);

	

}
