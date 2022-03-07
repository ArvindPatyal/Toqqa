package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

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
	
	
	

}
