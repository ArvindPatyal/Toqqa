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

import com.toqqa.bo.CartBo;
import com.toqqa.payload.CartPayload;
import com.toqqa.payload.CartUpdate;
import com.toqqa.payload.Response;
import com.toqqa.service.CartService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@ApiOperation(value = "Add Product To Cart")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/addtocart")
	public Response<CartBo> addToCart(@RequestBody @Valid CartPayload cartPayload) {
		log.info("Inside Controller Add To Cart");
		return new Response<CartBo>(this.cartService.addToCart(cartPayload), "success");
	}

	@ApiOperation(value = "Fetch Product from Cart")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchcart/{id}")
	public Response<CartBo> fetchCartItems(@PathVariable("id") @Valid String id) {
		log.info("Inside Controller fetch cart items");
		return new Response<CartBo>(this.cartService.fetchCart(id), "success");
	}

	@ApiOperation(value = "Update Product To Cart")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PutMapping("/updatecart")
	public Response<CartBo> updateCart(@RequestBody @Valid CartUpdate cartUpdate) {
		log.info("Inside Controller Add To Cart");
		return new Response<CartBo>(this.cartService.updateCart(cartUpdate), "success");
	}
}
