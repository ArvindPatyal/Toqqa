package com.toqqa.controller;

import com.toqqa.payload.ProductRatingPayload;
import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SellerRatingPayload;
import com.toqqa.service.ProductRatingService;
import com.toqqa.service.SellerRatingService;
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

    @PostMapping(value = "/product")
    public Response rateProduct(@RequestBody @Valid ProductRatingPayload productRatingPayload) {
        log.info("Invoked :: RatingController :: rateProduct()");
        return this.productRatingService.rateProduct(productRatingPayload);
    }

    @PostMapping(value = "/seller")
    public Response rateSeller(@RequestBody @Valid SellerRatingPayload sellerRatingPayload) {
        log.info("Invoked :: RatingController :: rateSeller()");
        return this.sellerRatingService.rateSeller(sellerRatingPayload);
    }

    @GetMapping(value = "/product/{productId}")
    public Response productRatings(@PathVariable String productId) {
        log.info("Invoked :: RatingController :: ratingDetails()");
        return this.productRatingService.productRatings(productId);
    }

    @GetMapping(value = "/seller/{sellerId}")
    public Response sellerRatings(@PathVariable String sellerId) {
        log.info("Invoked :: RatingController :: sellerRatings()");
        return this.sellerRatingService.sellerRatings(sellerId);
    }

    @DeleteMapping(value = "/product/{ratingId}")
    public Response deleteProductRating(@PathVariable String ratingId) {
        log.info("Invoked :: RatingController :: deleteProductRating()");
        return this.productRatingService.deleteProductRating(ratingId);
    }

    @DeleteMapping(value = "/seller/{ratingId}")
    public Response deleteSellerRating(@PathVariable String ratingId) {
        log.info("Invoked :: RatingController :: deleteSellerRating()");
        return this.sellerRatingService.deleteSellerRating(ratingId);
    }

    @PutMapping(value = "/product")
    public Response updateProductRating(@RequestBody @Valid RatingUpdatePayload ratingUpdatePayload) {
        log.info("Invoked :: RatingController :: updateProductRating()");
        return this.productRatingService.updateProductRating(ratingUpdatePayload);
    }

    @PutMapping(value = "/seller")
    public Response updateSellerRating(@RequestBody @Valid RatingUpdatePayload ratingUpdatePayload) {
        log.info("Invoked :: RatingController :: updateSellerRating()");
        return this.sellerRatingService.updateSellerRating(ratingUpdatePayload);
    }
}
