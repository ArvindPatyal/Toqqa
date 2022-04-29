package com.toqqa.bo;

import com.toqqa.domain.Favourite;
import lombok.Data;

import java.util.List;

@Data
public class FavouriteBo {
    private String id;
    private List<FavouriteSmeBo> favouriteSmeBoList;

    public FavouriteBo(Favourite favourite, List<FavouriteSmeBo> favouriteSmeBoList) {
        this.id = favourite.getId();
        this.favouriteSmeBoList = favouriteSmeBoList;
    }
}
