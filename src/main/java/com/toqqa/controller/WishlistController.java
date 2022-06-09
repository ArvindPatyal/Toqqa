package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;
import com.toqqa.service.WishlistService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

	@Autowired
	WishlistService wishlistService;

	@ApiOperation(value = "create wishlist")
	@PostMapping("/create")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	public Response toggleWishlist(@RequestBody @Valid WishlistItemPayload wishlistItemPayload) {
		log.info("Invoked:: WishlistController:: toggleWishlist");
		return wishlistService.toggleWishlist(wishlistItemPayload);
	}

	@ApiOperation(value = " view wishlist")
	@GetMapping("/fetchList")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	public ListResponse fetchWishlist() {
		log.info("Invoked:: WishlistController:: fetchWishlist");
		return wishlistService.fetchWishlist();

	}

	@ApiOperation(value = "delete from wishlist")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@DeleteMapping("/deleteItem/{productId}")
	public Response deleteWishlist(@PathVariable("productId") @Valid String productId) {
		log.info("Invoked:: WishlistController:: deleteWishlist");
		wishlistService.deleteWishlistItem(productId);
		return new Response(true, "item removed successfully");
	}
}
