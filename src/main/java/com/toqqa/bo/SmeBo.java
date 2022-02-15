package com.toqqa.bo;

import java.util.List;

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
public class SmeBo {
	private String id;
	private String nameOfBusiness;
	private String businessAddress;
	private String city;
	private String state;
	private String country;
	private String businessLogo;
	private String description;
	private List<Category> businessCatagory;
	private List<SubCategory> businessSubCatagory;
	private Boolean isDeleted;
	private String typeOfBusiness;
	private Boolean isDeliverToCustomer;
	private Double deliveryCharges;
	private Boolean isRegisterWithGovt;
	private Double deliveryRadius;
	private Long timeOfDelivery;
	private String regDoc;
	private String idProof;

	public SmeBo(Sme sme) {
		this.id = sme.getId();
		this.nameOfBusiness = sme.getNameOfBusiness();
		this.city = sme.getCity();
		this.state = sme.getState();
		this.country = sme.getCountry();
		this.businessLogo = sme.getBusinessLogo();
		this.description = sme.getDescription();
		this.businessCatagory = sme.getBusinessCatagory();
		this.businessSubCatagory = sme.getBusinessSubCatagory();
		this.isDeleted = sme.getIsDeleted();
		this.typeOfBusiness = sme.getTypeOfBusiness();
		this.isDeliverToCustomer = sme.getIsDeliverToCustomer();
		this.deliveryCharges = sme.getDeliveryCharges();
		this.isRegisterWithGovt = sme.getIsRegisterWithGovt();
		this.deliveryRadius = sme.getDeliveryRadius();
		this.timeOfDelivery = sme.getTimeOfDelivery();
		this.regDoc = sme.getRegDoc();
		this.idProof = sme.getIdProof();
		this.businessAddress = sme.getBusinessAddress();

	}

}
