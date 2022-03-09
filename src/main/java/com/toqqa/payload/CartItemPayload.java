package com.toqqa.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemPayload {

	@NotNull
	@NotEmpty
	private Double amount;

	@NotNull
	@NotEmpty
	private String productId;

	@NotNull
	@NotEmpty
	private Integer quantity;

}
