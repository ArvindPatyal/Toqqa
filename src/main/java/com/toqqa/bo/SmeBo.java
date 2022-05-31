package com.toqqa.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.toqqa.domain.Sme;

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
	private List<CategoryBo> businessCatagories = new ArrayList<>();
	private List<SubCategoryBo> businessSubCatagories = new ArrayList<>();
	private Boolean isDeleted;
	private String typeOfBusiness;
	private Boolean isDeliverToCustomer;
	private Double deliveryCharges;
	private Boolean isRegisterWithGovt;
	private Double deliveryRadius;
	private Date startTimeOfDelivery;
	private Date endTimeOfDelivery;
	private String regDoc;
	private String idProof;
	private String userId;
	private Double latitude;
    private Double longitude;
	private Boolean isFavSme=false;

	public SmeBo(Sme sme) {
		this.id = sme.getId();
		this.nameOfBusiness = sme.getNameOfBusiness();
		this.city = sme.getCity();
		this.state = sme.getState();
		this.country = sme.getCountry();
		this.businessLogo = sme.getBusinessLogo();
		this.description = sme.getDescription();
		this.isDeleted = sme.getIsDeleted();
		this.typeOfBusiness = sme.getTypeOfBusiness();
		this.isDeliverToCustomer = sme.getIsDeliverToCustomer();
		this.deliveryCharges = sme.getDeliveryCharges();
		this.isRegisterWithGovt = sme.getIsRegisterWithGovt();
		this.deliveryRadius = sme.getDeliveryRadius();
		this.startTimeOfDelivery = sme.getStartTimeOfDelivery();
		this.endTimeOfDelivery=sme.getEndTimeOfDelivery();
		this.regDoc = sme.getRegDoc();
		this.idProof = sme.getIdProof();
		this.businessAddress = sme.getBusinessAddress();
		this.userId = sme.getUserId();
		this.latitude=sme.getLatitude();
		this.longitude=sme.getLongitude();
		sme.getBusinessCatagory().forEach(category -> businessCatagories.add(new CategoryBo(category)));
		sme.getBusinessSubCatagory().forEach(subCategory -> businessSubCatagories.add(new SubCategoryBo(subCategory)));
	}

}
