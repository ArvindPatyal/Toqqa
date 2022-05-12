package com.toqqa.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeliveryAddressUpdate {

	@NotNull
	private String city;

	@NotNull
	private String address;

	@NotNull
	private String postCode;

	@NotNull
	private String state;

	@NotNull
	private String country;

	@NotNull

	private String phoneNumber;

	@NotNull
	private String deliveryAddressId;
}
