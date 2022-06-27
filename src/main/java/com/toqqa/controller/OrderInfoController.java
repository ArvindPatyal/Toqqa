package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.constants.OrderStatus;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderCancelPayload;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.ToggleOrdersStatus;
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
	public Response placeOrder(@Valid @RequestBody OrderPayload orderPayload) {
		log.info("Invoked:: OrderInfoController:: placeOrder");
		return this.orderInfoService.placeOrder(orderPayload);
	}

	@ApiOperation(value = "cancel order")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/cancel")
	public Response<?> updateOrder(@Valid @RequestBody OrderCancelPayload cancelPayload) {
		log.info("Invoked:: OrderInfoController:: updateOrder");
		return this.orderInfoService.updateOrder(cancelPayload);
	}

	@ApiOperation(value = "Returns Product data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchOrderInfo/{id}")
	public Response<OrderInfoBo> fetchOrderInfo(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: OrderInfoController:: fetchOrderInfo");
		return new Response<OrderInfoBo>(this.orderInfoService.fetchOrderInfo(id), "success");
	}

	@ApiOperation(value = "list of orders placed by customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/fetchOrderList")
	public ListResponseWithCount<OrderInfoBo> fetchOrderList(@RequestBody @Valid PaginationBo paginationbo) {
		log.info("Invoked:: OrderInfoController:: fetchOrderList");
		return this.orderInfoService.fetchOrderList(paginationbo);
	}

	/*
	 * @GetMapping(value = "/fetchInvoice/{orderId}") public Response
	 * fetchInvoice(@PathVariable @Valid String orderId) {
	 * log.info("Inside Controller fetch Invoice"); return new
	 * Response(this.orderInfoService.orderInvoice(orderId), "Invoice Generated"); }
	 */

	@ApiOperation(value = "Orders list of Customer with respect to SME (Live & Cancelled)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/list")
	public ListResponseWithCount<OrderInfoBo> list(@RequestBody @Valid ToggleOrdersStatus toggleOrdersStatus) {
		log.info("Invoked:: OrderInfoController:: list");
		return this.orderInfoService.list(toggleOrdersStatus);
	}

	@ApiOperation(value = "Specific order Info ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/order/{orderId}")
	public Response orderItems(@PathVariable String orderId) {
		log.info("Invoked:: OrderInfoController:: orderItems");
		return new Response(orderInfoService.orderDetails(orderId), "");
	}

	@ApiOperation(value = "update order status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/updateStatus")
	public Response orderStatus(@RequestBody @Valid OrderStatusUpdatePayload orderStatusUpdatePayload) {
		log.info("Invoked:: OrderInfoController:: orderStatus");
		return this.orderInfoService.updateOrderStatus(orderStatusUpdatePayload);
	}
}
