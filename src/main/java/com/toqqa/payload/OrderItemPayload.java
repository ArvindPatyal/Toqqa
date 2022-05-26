package com.toqqa.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemPayload {

	@NotNull
	@NotBlank
	private Integer quantity;

	@NotNull
	private Double price;

	@NotBlank
	@NotNull
	private String productId;

	@NotNull
	private String sellerUserId;

	@NotNull
	private Double shippingFee;

}
