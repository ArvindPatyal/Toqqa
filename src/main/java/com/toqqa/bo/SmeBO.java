package com.toqqa.bo;

import com.toqqa.domain.Category;
import com.toqqa.domain.Sme;
import com.toqqa.domain.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmeBO {
	private String id;
	private String nameOfBusiness;
	private String shopNumber;
	private String street;
	private String city;
	private String state;
	private String country;
	private String businessLogo;
	private Category businessCatagory;
	private SubCategory businessSubCatagory;
	private String description;
	private boolean isDeleted;
	private String typeOfBusiness;
	private boolean isDeliverToCustomer;
	private double deliveryCharges;
	private boolean isRegisterWithGovt;

	public void SmeBo(Sme sme) {
		this.id = sme.getId();
		this.nameOfBusiness = sme.getNameOfBusiness();
		this.shopNumber = sme.getShopNumber();
		this.street = sme.getStreet();
		this.city = sme.getCity();
		this.state = sme.getState();
		this.country = sme.getCountry();
		this.businessLogo = sme.getBusinessLogo();
		this.businessCatagory = sme.getBusinessCatagory();
		this.businessSubCatagory = sme.getBusinessSubCatagory();
		this.description = sme.getDescription();
		this.isDeleted = sme.isDeleted();
		this.typeOfBusiness = sme.getTypeOfBusiness();
		this.isDeliverToCustomer = sme.isDeliverToCustomer();
		this.deliveryCharges = sme.getDeliveryCharges();
		this.isRegisterWithGovt = sme.isRegisterWithGovt();

	}

}
