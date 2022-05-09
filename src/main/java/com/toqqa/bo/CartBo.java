package com.toqqa.bo;

import java.util.Date;
import java.util.List;

import com.toqqa.domain.Cart;

import lombok.Data;

@Data
public class CartBo {

	private String id;

	private UserBo user;

	private Date createdDate;

	private Date modificationDate;
	
	private Double subTotal;

	private List<CartItemBo> items;

	public CartBo(Cart cart, List<CartItemBo> items) {
		this.id = cart.getId();
		this.user = new UserBo(cart.getUser());
		this.createdDate = cart.getCreatedDate();
		this.modificationDate = cart.getModificationDate();
		this.items = items;
	}

}
