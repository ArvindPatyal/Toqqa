package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddressUpdate {

	@NotNull
	private String city;

	@NotNull
	private String street;

	@NotNull
	private String postCode;

	@NotNull
	private String state;

	@NotNull
	private String country;

	@NotNull
	private String houseNumber;

	@NotNull
	private String deliveryAddressId;
}
