package com.toqqa.bo;

import com.toqqa.domain.FavouriteSme;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteSmeBo {
    private String id;
    private SmeBo smeBo;

    public FavouriteSmeBo(FavouriteSme favouriteSme) {
        this.id = favouriteSme.getSmeId();
        this.smeBo = new SmeBo(favouriteSme.getSme());
    }
}
