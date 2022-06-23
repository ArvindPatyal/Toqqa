package com.toqqa.service;

import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SellerRatingPayload;

public interface SellerRatingService {


    Response rateSeller(SellerRatingPayload sellerRatingPayload);

    Response sellerRatings(String sellerId);

    Response deleteSellerRating(String sellerId);

    Response updateSellerRating(RatingUpdatePayload ratingUpdatePayload);
}
