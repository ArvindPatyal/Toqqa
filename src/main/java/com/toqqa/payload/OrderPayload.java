package com.toqqa.payload;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPayload {

	@Valid
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

}
