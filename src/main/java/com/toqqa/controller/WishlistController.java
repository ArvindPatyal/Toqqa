package com.toqqa.controller;

import com.toqqa.bo.WishlistBo;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;
import com.toqqa.service.WishlistService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("api/user/wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @ApiOperation(value = "create wishlist")
    @PostMapping("/create")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response<WishlistBo> createWishlist(@RequestBody @Valid WishlistItemPayload wishlistItemPayload) {
        log.info("Inside Controller create wishlist");
        return new Response<WishlistBo>(wishlistService.createWishlist(wishlistItemPayload), "Sucess");
    }

    @ApiOperation(value = " view wishlist")
    @GetMapping("/userWishlist")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response<WishlistBo> showWishlist() {
        log.info("Inside Controller view Wishlist");
        return new Response<WishlistBo>(wishlistService.fetchWishlist(), "Sucess");

    }


    @ApiOperation(value = "delete from wishlist")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping("/deleteItem/{productId}")
    public void deleteWishlist(@PathVariable("productId") @Valid String productId) {
        log.info("Inside Controller delete wishlist");
        wishlistService.deleteWishlistItem(productId);
    }
}

