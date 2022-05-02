package com.toqqa.service;

import com.toqqa.bo.FavouriteBo;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.Response;

public interface FavouriteService {

    Response addSme(FavouriteSmePayload favouriteSmePayload);

    FavouriteBo fetchFavourite();

    Response removeSme(String smeId);


}
