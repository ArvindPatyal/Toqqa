package com.toqqa.controller;

import com.toqqa.payload.ProductRatingPayload;
import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SellerRatingPayload;
import com.toqqa.service.ProductRatingService;
import com.toqqa.service.SellerRatingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(value = "api/rating")
public class RatingController {
    @Autowired
    private ProductRatingService productRatingService;

    @Autowired
    private SellerRatingService sellerRatingService;

    @ApiOperation(value = "To rate a product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/product")
    public Response rateProduct(@RequestBody @Valid ProductRatingPayload productRatingPayload) {
        log.info("Invoked :: RatingController :: rateProduct()");
        return this.productRatingService.rateProduct(productRatingPayload);
    }

    @ApiOperation(value = "To rate a seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/seller")
    public Response rateSeller(@RequestBody @Valid SellerRatingPayload sellerRatingPayload) {
        log.info("Invoked :: RatingController :: rateSeller()");
        return this.sellerRatingService.rateSeller(sellerRatingPayload);
    }

    @ApiOperation(value = "Ratings of a specific product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/product/{productId}")
    public Response productRatings(@PathVariable String productId) {
        log.info("Invoked :: RatingController :: ratingDetails()");
        return this.productRatingService.productRatings(productId);
    }

    @ApiOperation(value = "Ratings of a specific seller by smeId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/seller/{sellerId}")
    public Response sellerRatings(@PathVariable String sellerId) {
        log.info("Invoked :: RatingController :: sellerRatings()");
        return this.sellerRatingService.sellerRatings(sellerId);
    }

    @ApiOperation(value = "delete a single rating for a product via ratingId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping(value = "/product/{ratingId}")
    public Response deleteProductRating(@PathVariable String ratingId) {
        log.info("Invoked :: RatingController :: deleteProductRating()");
        return this.productRatingService.deleteProductRating(ratingId);
    }

    @ApiOperation(value = "delete a single rating for a seller via ratingId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping(value = "/seller/{ratingId}")
    public Response deleteSellerRating(@PathVariable String ratingId) {
        log.info("Invoked :: RatingController :: deleteSellerRating()");
        return this.sellerRatingService.deleteSellerRating(ratingId);
    }

    @ApiOperation(value = "Update a single product rating via ratingId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping(value = "/product")
    public Response updateProductRating(@RequestBody @Valid RatingUpdatePayload ratingUpdatePayload) {
        log.info("Invoked :: RatingController :: updateProductRating()");
        return this.productRatingService.updateProductRating(ratingUpdatePayload);
    }

    @ApiOperation(value = "Update a single seller rating via ratingId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping(value = "/seller")
    public Response updateSellerRating(@RequestBody @Valid RatingUpdatePayload ratingUpdatePayload) {
        log.info("Invoked :: RatingController :: updateSellerRating()");
        return this.sellerRatingService.updateSellerRating(ratingUpdatePayload);
    }
}
