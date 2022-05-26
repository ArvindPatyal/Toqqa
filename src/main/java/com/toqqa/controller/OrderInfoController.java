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
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/placeOrder")
    public Response placeOrder(@RequestBody @Valid OrderPayload orderPayload) {
        log.info("Inside controller add order");
        return this.orderInfoService.placeOrder(orderPayload);
    }

    @ApiOperation(value = "Returns Product data by given id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
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

    @GetMapping(value = "/fetchInvoice/{orderId}")
    public Response fetchInvoice(@PathVariable @Valid String orderId) {
        log.info("Inside Controller fetch Invoice");
        return new Response(this.orderInfoService.orderInvoice(orderId), "Invoice Generated");
    }


}
