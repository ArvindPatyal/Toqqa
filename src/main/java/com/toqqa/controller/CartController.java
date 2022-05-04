package com.toqqa.controller;

import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.CartItemPayload;
import com.toqqa.payload.CartUpdatePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.service.CartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @ApiOperation(value = "manage cart addition or updation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/addtocart")
    public Response addToCart(@RequestBody @Valid CartItemPayload cartItemPayload) {
        log.info("Inside Controller Add To Cart");
        return this.cartService.manageCart(cartItemPayload);
    }

    @ApiOperation(value = "Fetch Product from Cart")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/fetchcart")
    public ListResponse fetchCartItems(@RequestBody @Valid PaginationBo paginationBo) {
        log.info("Inside Controller fetch cart items");
        return this.cartService.fetchCart(paginationBo);
    }

    @ApiOperation(value = "Update Product To Cart")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping("/updatecart")
    public Response updateCart(@RequestBody @Valid CartUpdatePayload cartUpdatePayload) {
        log.info("Inside Controller Add To Cart");
        return this.cartService.updateCart(cartUpdatePayload);
    }

    @ApiOperation(value = "remove an item from cart")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucess"), @ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping("/delete/{productId}")
    public Response deleteitem(@PathVariable @Valid String productId) {
        log.info("Inside controller delete cart item");
        return cartService.deleteCartItem(productId);
    }
}
