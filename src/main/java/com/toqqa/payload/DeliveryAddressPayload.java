package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddressPayload {

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

}
