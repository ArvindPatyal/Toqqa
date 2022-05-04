package com.toqqa.service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.CartItemPayload;
import com.toqqa.payload.CartUpdatePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;

public interface CartService {

    Response manageCart(CartItemPayload cartPayItemload);

    Response updateCart(CartUpdatePayload cartUpdatePayload);

    Response deleteCartItem(String productId);

    ListResponse fetchCart(PaginationBo paginationBo);
}
