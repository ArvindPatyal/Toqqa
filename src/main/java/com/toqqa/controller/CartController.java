package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.payload.CartItemPayload;
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

	@ApiOperation(value = "manage cart addition or updation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/managecart")
	public Response manageCart(@RequestBody @Valid CartItemPayload cartItemPayload) {
		log.info("Invoked:: CartController:: manageCart");
		return this.cartService.manageCart(cartItemPayload);
	}

	@ApiOperation(value = "Fetch cart details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchcart")
	public Response fetchCart() {
		log.info("Invoked:: CartControllerr:: fetchCart");
		return this.cartService.fetchCart();
	}

	@ApiOperation(value = "Update Product To Cart")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PutMapping("/updatecart")
	public Response updateCart(@RequestBody @Valid CartItemPayload cartItemPayload) {
		log.info("Invoked:: CartController:: updateCart");
		return this.cartService.updateCart(cartItemPayload);
	}

	@ApiOperation(value = "remove an item from cart")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucess"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@DeleteMapping("/delete/{productId}")
	public Response deleteitem(@PathVariable @Valid String productId) {
		log.info("Invoked:: CartController:: deleteitem");
		return cartService.deleteCartItem(productId);
	}
}
