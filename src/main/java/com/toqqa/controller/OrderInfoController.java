package com.toqqa.controller;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.constants.OrderConstants;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.*;
import com.toqqa.service.OrderInfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
		log.info("Inside controller add order");
		return this.orderInfoService.placeOrder(orderPayload);
	}

	@ApiOperation(value = "cancel order")
	@ApiResponses(value = {@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!")})
	@PutMapping("/cancel/{orderId}")
	public Response<?> updateOrder(@PathVariable @Valid String orderId) {
		log.info("Inside controller cancel order");
		return this.orderInfoService.updateOrder(orderId);
	}

	@ApiOperation(value = "Returns Product data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchOrderInfo/{id}")
	public Response<OrderInfoBo> fetchOrderInfo(@PathVariable("id") @Valid String id) {
		log.info("Inside controller fetch product");
		return new Response<OrderInfoBo>(this.orderInfoService.fetchOrderInfo(id), "success");
	}

	@ApiOperation(value = "list of orders placed by customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/fetchOrderList")
	public ListResponseWithCount<OrderInfoBo> fetchOrderList(@RequestBody @Valid PaginationBo paginationbo) {
		log.info("Inside controller fetch Order List");
		return this.orderInfoService.fetchOrderList(paginationbo);
	}

/*	@GetMapping(value = "/fetchInvoice/{orderId}")
	public Response fetchInvoice(@PathVariable @Valid String orderId) {
		log.info("Inside Controller fetch Invoice");
		return new Response(this.orderInfoService.orderInvoice(orderId), "Invoice Generated");
	}*/

	@ApiOperation(value = "Orders list of Customer with respect to SME (Live & Cancelled)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/list")
	public ListResponseWithCount<OrderInfoBo> list(@RequestBody @Valid ToggleOrdersStatus toggleOrdersStatus) {
		log.info("Inside controller fetch Order List");
		return this.orderInfoService.list(toggleOrdersStatus);
	}

	@ApiOperation(value = "Specific order Info ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/order/{orderId}")
	public Response orderItems(@PathVariable String orderId) {
		log.info("Inside Controller Sme order items");
		return new Response(orderInfoService.orderDetails(orderId), "");
	}

	@ApiOperation(value = "update order status")
	@ApiResponses(value = {@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request")})
	@PostMapping("/updateStatus")
	public Response orderStatus(@RequestBody @Valid OrderStatusUpdatePayload orderStatusUpdatePayload) {
		log.info("Inside Controller update order status");
		try {
			OrderConstants.valueOf(orderStatusUpdatePayload.getOrderConstant());
		} catch (Exception e) {
			throw new BadRequestException("invalid order constant " + orderStatusUpdatePayload.getOrderConstant());
		}
		return this.orderInfoService.updateOrderStatus(orderStatusUpdatePayload);
	}
}
