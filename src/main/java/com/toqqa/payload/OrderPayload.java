package com.toqqa.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderPayload {

	@NotEmpty
	private List<OrderItemPayload> items;

	@NotNull
	private Double amount;

	// @NotNull
	private String email;

	@NotNull
	private String phone;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	@NotBlank
	private String addressId;

	private Double shippingFee;

}
