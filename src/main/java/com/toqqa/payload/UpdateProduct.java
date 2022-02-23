package com.toqqa.payload;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateProduct {

	@NotNull
	private String productId;

	@NotNull
	private String productName;

	@NotNull
	private List<String> productCategory;

	@NotNull
	private List<String> productSubCategory;

	@NotNull
	private MultipartFile image;

	@NotNull
	private String description;

	@NotNull
	private String details;

	@NotNull
	private Long unitsInStock;

	@NotNull
	private Double pricePerUnit;

	@NotNull
	private Double discount;

	private Integer maximumUnitsInOneOrder;

	private Integer minimumUnitsInOneOrder;

	@NotNull
	private Date expiryDate;

	@NotNull
	private String countryOfOrigin;

	@NotNull
	private String manufacturerName;

// To Do Awaiting Feedback Around These Two Fields..

//	private Boolean doYouDelieverInSpecifiedRadius;

//	private Boolean delieveredOutsideOfDelieveryRadius;

}
