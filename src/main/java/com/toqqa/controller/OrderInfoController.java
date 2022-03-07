package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.Response;
import com.toqqa.service.OrderInfoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderInfoController {

	@Autowired
	OrderInfoService orderInfoService;

	@ApiOperation(value = "Add Order")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/placeOrder")
	public Response<OrderInfoBo> placeOrder(@RequestBody @Valid OrderPayload orderPayload) {
		log.info("Inside controller add order");
		return new Response<OrderInfoBo>(this.orderInfoService.placeOrder(orderPayload), "success");

	}
}
