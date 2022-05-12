package com.toqqa.bo;

import com.toqqa.domain.DeliveryAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressBo {

	private String city;

	private String postCode;

	private String state;

	private String country;

	private String address;

	private String phoneNumber;

	public DeliveryAddressBo(DeliveryAddress address) {

		this.address = address.getAddress();
		this.city = address.getCity();
		this.postCode = address.getPostCode();
		this.state = address.getState();
		this.country = address.getCountry();
		this.phoneNumber = address.getPhoneNumber();
	}

}
