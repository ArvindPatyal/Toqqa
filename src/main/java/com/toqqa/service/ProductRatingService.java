package com.toqqa.service;

import com.toqqa.payload.ProductRatingPayload;
import com.toqqa.payload.ProductRatings;
import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;

public interface ProductRatingService {
    Response rateProduct(ProductRatingPayload productRatingPayload);

    Response productRatings(ProductRatings ProductRatings);

    Response deleteProductRating(String productId);

    Response updateProductRating(RatingUpdatePayload ratingUpdatePayload);
}
