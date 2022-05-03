package com.toqqa.controller;

import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.ListResponse;
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
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @ApiOperation(value = "create wishlist")
    @PostMapping("/create")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response toggleWishlist(@RequestBody @Valid WishlistItemPayload wishlistItemPayload) {
        log.info("Inside Controller create wishlist");
        return wishlistService.toggleWishlist(wishlistItemPayload);
    }

    @ApiOperation(value = " view wishlist")
    @PostMapping("/fetchList")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public ListResponse fetchWishlist(@RequestBody @Valid PaginationBo bo) {
        log.info("Inside Controller view Wishlist");
        return wishlistService.fetchWishlist(bo);

    }


    @ApiOperation(value = "delete from wishlist")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping("/deleteItem/{productId}")
    public void deleteWishlist(@PathVariable("productId") @Valid String productId) {
        log.info("Inside Controller delete wishlist");
        wishlistService.deleteWishlistItem(productId);
    }
}

