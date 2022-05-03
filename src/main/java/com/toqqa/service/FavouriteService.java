package com.toqqa.service;

import com.toqqa.bo.FavouriteBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;

public interface FavouriteService {

    Response addSme(FavouriteSmePayload favouriteSmePayload);

    ListResponse<SmeBo> fetchFavoriteList(PaginationBo bo);

    Response removeSme(String smeId);


}
