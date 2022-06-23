package com.toqqa.repository;

import com.toqqa.domain.SellerRating;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRatingRepository extends JpaRepository<SellerRating,String> {
    SellerRating findByIdAndUser_Id(String ratingId,String userId);

    SellerRating findBySmeIdAndUser_Id(String smeId, String user);
}
