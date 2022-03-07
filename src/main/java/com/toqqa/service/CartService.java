package com.toqqa.service;

import com.toqqa.bo.CartBo;
import com.toqqa.payload.CartPayload;
import com.toqqa.payload.CartUpdate;

public interface CartService {

	CartBo addToCart(CartPayload cartPayload);

	CartBo updateCart(CartUpdate cartUpdate);

	CartBo fetchCart(String id);
}
