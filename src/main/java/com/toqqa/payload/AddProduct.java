package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddProduct {

	@NotNull
	private String productName;

	@NotNull
	private List<String> productCategory;

//	@NotNull
	private List<String> productSubCategory;

	@NotNull
	private List<MultipartFile> images;

	@NotNull
	private String description;

	/*
	 * @NotNull private String details;
	 */

	@NotNull
	private Long unitsInStock;

	@NotNull
	private Double pricePerUnit;

	@NotNull
	@DecimalMax("100.00")
	@DecimalMin("0.00")
	private Double discount;

	private Integer maximumUnitsInOneOrder;

	private Integer minimumUnitsInOneOrder;

//	@NotNull
	private Long expiryDate;

	@NotNull
	private String countryOfOrigin;

	@NotNull
	private String manufacturerName;

	@NotNull
	private Long manufacturingDate;

	@NotNull
	private MultipartFile banner;

	private Boolean deliveredInSpecifiedRadius;

	private Boolean delieveredOutsideSpecifiedRadius;

}
