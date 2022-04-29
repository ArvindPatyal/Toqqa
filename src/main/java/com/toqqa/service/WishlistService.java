package com.toqqa.service;

import com.toqqa.bo.WishlistBo;
import com.toqqa.bo.WishlistItemBo;
import com.toqqa.domain.Wishlist;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;

import java.util.List;


public interface WishlistService {
    Response toggleWishlist(WishlistItemPayload wishlistItemPayload);

    List<WishlistItemBo> fetchWishlistItems(Wishlist wishlist);

    void deleteWishlistItem(String productId);

    WishlistBo fetchWishlist();
}
