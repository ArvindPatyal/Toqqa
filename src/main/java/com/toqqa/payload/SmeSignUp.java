package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmeSignUp {
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

	@NotEmpty
	private MultipartFile businessLogo;

	@NotNull
	private List<String> businessCategory;

	@NotNull
	private List<String> businessSubCategory;

	@NotNull
	private String typeOfBusiness;

	@NotNull
	private Boolean deliverToCustomer;

	private Double deliveryRadius;

	@NotNull
	private Double deliveryCharge;

	@NotNull
	private Boolean isRegisteredWithGovt;

	@NotEmpty
	private MultipartFile regDoc;

	@NotEmpty
	private MultipartFile idProof;

	@NotNull
	@NotBlank
	private String userId;

}
