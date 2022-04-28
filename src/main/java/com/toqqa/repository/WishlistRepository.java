package com.toqqa.repository;


import com.toqqa.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {


//    Wishlist findByUser(User user);

    Wishlist findWishlistIdByUser_id(String userid);

    Optional<Wishlist> findByUserId(String userid);

    Wishlist findByUser_id(String id);
}
