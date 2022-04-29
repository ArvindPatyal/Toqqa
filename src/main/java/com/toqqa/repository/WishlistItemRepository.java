package com.toqqa.repository;

import com.toqqa.domain.Wishlist;
import com.toqqa.domain.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {
    List<WishlistItem> findByWishlist(Wishlist wishlist);

    void deleteByProductIdAndWishlist_Id(String productId, String wishlistId);

}
