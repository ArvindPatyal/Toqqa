package com.toqqa.service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.bo.WishlistBo;
import com.toqqa.bo.WishlistItemBo;
import com.toqqa.domain.Wishlist;
import com.toqqa.payload.ListProductRequest;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;

import java.util.List;


public interface WishlistService {

    Response toggleWishlist(WishlistItemPayload wishlistItemPayload);

//    Boolean isWishListItem(List<ProductBo> productBos, Wishlist wishlist);

    Boolean isWishListItem(ProductBo productBo, Wishlist wishlist);

    void deleteWishlistItem(String productId);

    ListResponse fetchWishlist(PaginationBo bo);
}
