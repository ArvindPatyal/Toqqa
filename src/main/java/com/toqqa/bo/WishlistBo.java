package com.toqqa.bo;

import com.toqqa.domain.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistBo {

    private String id;

    private List<WishlistItemBo> wishlistItems;

    public WishlistBo(Wishlist wishlist, List<WishlistItemBo> wishlistItems) {
        this.id = wishlist.getId();
        this.wishlistItems = wishlistItems;


    }


}
