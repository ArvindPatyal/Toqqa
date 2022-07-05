package com.toqqa.service;

import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SellerRatingPayload;
import com.toqqa.payload.SellerRatings;

public interface SellerRatingService {


    Response rateSeller(SellerRatingPayload sellerRatingPayload);

    Response sellerRatings(SellerRatings sellerRatings);

    Response deleteSellerRating(String sellerId);

    Response updateSellerRating(RatingUpdatePayload ratingUpdatePayload);
}
