package com.toqqa.controller;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderPayload;
import com.toqqa.payload.Response;
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
	public Response<OrderInfoBo> placeOrder(@RequestBody @Valid OrderPayload orderPayload) {
		log.info("Inside controller add order");
		return new Response<OrderInfoBo>(this.orderInfoService.placeOrder(orderPayload), "success");
	}

	@ApiOperation(value = "Returns Product data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchOrderInfo/{id}")
	public Response<OrderInfoBo> fetchOrderInfo(@PathVariable("id") @Valid String id) {
		log.info("Inside controller fetch product");
		return new Response<OrderInfoBo>(this.orderInfoService.fetchOrderInfo(id), "success");
	}

	@PostMapping("/fetchOrderList")
	public ListResponseWithCount<OrderInfoBo> fetchOrderList(@RequestBody @Valid PaginationBo paginationbo) {
		log.info("Inside controller fetch Order List");
		return this.orderInfoService.fetchOrderList(paginationbo);
	}

//	@PostMapping(value = "/generateInvoice/{orderid}",produces = MediaType.APPLICATION_PDF_VALUE)
//	public byte[] generateInvoice(@PathVariable @Valid String orderid) {
//		log.info("Inside controller generate Invoice");
//		this.orderInfoService.orderInvoice(orderid);
//		try
//		{
//			FileInputStream fileInputStream= new FileInputStream("invoice.pdf");
//			byte[] byteArray = new byte[fileInputStream.available()];
//			fileInputStream.read(byteArray);
//			return byteArray;
//
//		} catch (IOException e) {
//
//		}
//		return null;
//	}
@GetMapping(value = "/generateInvoice/{orderId}")
public Response fetchInvoice(@PathVariable @Valid String orderId) {
	log.info("Inside Controller fetch Invoice");
	return new Response(this.orderInfoService.orderInvoice(orderId), "Invoice Generated");
}


}
