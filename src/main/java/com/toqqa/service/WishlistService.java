package com.toqqa.service;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Wishlist;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;


public interface WishlistService {

    Response toggleWishlist(WishlistItemPayload wishlistItemPayload);

//    Boolean isWishListItem(List<ProductBo> productBos, Wishlist wishlist);

    Boolean isWishListItem(ProductBo productBo, Wishlist wishlist);

    void deleteWishlistItem(String productId);

    ListResponse fetchWishlist();
}
