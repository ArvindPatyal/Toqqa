package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddProduct {

	@NotNull
	@NotBlank
	private String productName;

	@NotNull
	private List<String> productCategory;

//	@NotNull
	private List<String> productSubCategory;

	@NotNull
	private List<MultipartFile> images;

	@NotNull
	@Length(min = 1, max = 300)
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
