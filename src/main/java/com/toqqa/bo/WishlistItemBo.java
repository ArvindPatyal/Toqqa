package com.toqqa.bo;

import com.toqqa.domain.WishlistItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemBo {

    private String id;

    private ProductBo product;

    public WishlistItemBo(WishlistItem wishlistItem) {
        this.id = wishlistItem.getId();
        this.product = new ProductBo(wishlistItem.getProduct());
        ;
    }
}
