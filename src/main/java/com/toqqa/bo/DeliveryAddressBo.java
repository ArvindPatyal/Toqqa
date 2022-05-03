package com.toqqa.bo;

import com.toqqa.domain.DeliveryAddress;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressBo {

	private String city;

	private String street;

	private String postCode;

	private String state;

	private String country;

	private String houseNumber;

	public DeliveryAddressBo(DeliveryAddress address) {

		this.street = address.getStreet();
		this.city = address.getCity();
		this.postCode = address.getPostCode();
		this.state = address.getState();
		this.country = address.getCountry();
		this.houseNumber = address.getHouseNumber();
	}

}
