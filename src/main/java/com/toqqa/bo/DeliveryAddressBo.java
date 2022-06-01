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

	private String id;

	private String city;

	private String postCode;

	private String state;

	private String country;

	private String address;

	private String phoneNumber;

	private Double latitude;

	private Double longitude;

	private Boolean isCurrentAddress;


	public DeliveryAddressBo(DeliveryAddress address) {

		this.address = address.getAddress();
		this.city = address.getCity();
		this.postCode = address.getPostCode();
		this.state = address.getState();
		this.country = address.getCountry();
		this.phoneNumber = address.getPhoneNumber();
		this.id = address.getId();
		this.isCurrentAddress = address.getIsCurrentAddress();
		this.latitude = address.getLatitude();
		this.longitude = address.getLongitude();
	}

}
