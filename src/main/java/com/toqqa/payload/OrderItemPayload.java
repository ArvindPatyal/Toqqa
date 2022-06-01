package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemPayload {

	@NotNull
	private Integer quantity;

	@NotNull
	private Double price;

	@NotNull
	private String productId;

	@NotNull
	private String sellerUserId;

	@NotNull
	private Double shippingFee;

}
