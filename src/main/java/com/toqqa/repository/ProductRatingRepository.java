package com.toqqa.repository;

import com.toqqa.domain.ProductRating;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRatingRepository extends JpaRepository<ProductRating, String> {

    ProductRating findByIdAndUser_Id(String id, String userId);

    ProductRating findByProductIdAndUser(String productId, User user);
}
