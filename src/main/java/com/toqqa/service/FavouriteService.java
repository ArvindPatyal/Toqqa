package com.toqqa.service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Favourite;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;

public interface FavouriteService {

    Response addFavouriteSme(FavouriteSmePayload favouriteSmePayload);

    ListResponse<SmeBo> fetchFavoriteList(PaginationBo bo);

    Boolean isFavSme(SmeBo bo, Favourite favourite);

    void removeFavoriteSme(String productUserId);


}
