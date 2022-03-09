package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CartUpdate {

	@NotNull
	private String productId;

	@NotNull
	private Integer quantity;

	@NotNull
	private String cartId;

//	@NotNull
//	private String userId;
}
