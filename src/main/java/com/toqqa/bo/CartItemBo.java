package com.toqqa.bo;

import com.toqqa.domain.CartItem;
import lombok.Data;

@Data
public class CartItemBo {

	private String id;


	private ProductBo product;

	private Integer quantity;

	public CartItemBo(CartItem cartItem,ProductBo product) {
		this.id = cartItem.getId();
		this.product = product;
		this.quantity = cartItem.getQuantity();
	}
}
