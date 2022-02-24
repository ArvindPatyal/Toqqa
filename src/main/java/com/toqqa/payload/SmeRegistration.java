package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmeRegistration {
	@NotNull
	private String nameOfBusiness;

	@NotNull
	private String businessAddress;

	@NotNull
	private String street;

	@NotNull
	private String city;

	@NotNull
	private String state;

	@NotNull
	private String country;

	@NotNull
	private MultipartFile businessLogo;

	@NotNull
	private List<String> businessCategory;

	@NotNull
	private List<String> businessSubCategory;

	private String description;

	@NotNull
	private String typeOfBusiness;

	@NotNull
	private Boolean deliverToCustomer;

	private Double deliveryRadius;

	@NotNull
	private Double deliveryCharge;

	private Long startTimeOfDelivery;

	private Long endTimeOfDelivery;

	@NotNull
	private Boolean isRegisteredWithGovt;

//	@NotNull
	private MultipartFile regDoc;

	@NotNull
	private MultipartFile idProof;

}
