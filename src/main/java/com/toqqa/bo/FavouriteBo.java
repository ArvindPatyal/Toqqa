package com.toqqa.bo;

import com.toqqa.domain.Favourite;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteBo {
    private String id;
    private List<FavouriteSmeBo> favouriteSmeBoList;

    public FavouriteBo(Favourite favourite, List<FavouriteSmeBo> favouriteSmeBoList) {
        this.id = favourite.getId();
        this.favouriteSmeBoList = favouriteSmeBoList;
    }
}
