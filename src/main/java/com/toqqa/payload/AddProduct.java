package com.toqqa.payload;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddProduct {

	@NotNull
	private String productName;

	@NotNull
	private String category;

	@NotNull
	private String subCategory;

	@NotNull
//	private List<Category> category;

	@NotNull
//	private List<SubCategory> subCategory;

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
