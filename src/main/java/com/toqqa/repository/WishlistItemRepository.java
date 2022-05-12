package com.toqqa.repository;

import com.toqqa.domain.Wishlist;
import com.toqqa.domain.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {

    List<WishlistItem> findByWishlist(Wishlist wishlist);

    void deleteByProductIdAndWishlist(String productId, Wishlist wishlist);
}
