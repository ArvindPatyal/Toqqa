package com.toqqa.bo;

import java.util.Date;

import com.toqqa.domain.Product;

import lombok.Data;

@Data
public class ProductBo {

	private String id;

	private String productName;

	// to Change category and sub category with product category and product sub
	// category
//	private List<Category> category;

//	private List<SubCategory> subCategory;

	private String category;

	private String subCategory;

	private String image;

	private String description;

	private String details;

	private Long unitsInStock;

	private Double pricePerUnit;

	private Double discount;

	private Integer maximumUitsInOneOrder;

	private Integer minimumUnitsInOneOrder;

	private Date expiryDate;

	private String countryOfOrigin;

	private String manufacturerName;

// To Do Awaiting Feedback Around These Two Fields..

//	private Boolean doYouDelieverInSpecifiedRadius;

//	private Boolean delieveredOutsideOfDelieveryRadius;

	public ProductBo(Product product) {
		this.id = product.getId();
		this.productName = product.getProductName();
		this.category = product.getCategory();
		this.subCategory = product.getSubCategory();
		this.image = product.getImage();
		this.description = product.getDescription();
		this.details = product.getDetails();
		this.unitsInStock = product.getUnitsInStock();
		this.pricePerUnit = product.getPricePerUnit();
		this.discount = product.getDiscount();
		this.maximumUitsInOneOrder = product.getMaximumUitsInOneOrder();
		this.minimumUnitsInOneOrder = product.getMinimumUnitsInOneOrder();
		this.expiryDate = product.getExpiryDate();
		this.countryOfOrigin = product.getCountryOfOrigin();
		this.manufacturerName = product.getManufacturerName();

	}

}
