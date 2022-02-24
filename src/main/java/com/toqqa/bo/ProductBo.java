package com.toqqa.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.toqqa.domain.Product;

import lombok.Data;

@Data
public class ProductBo {

	private String id;

	private String productName;

	private Map<String, String> productCategories = new HashMap<>();

	private Map<String, String> productSubCategories = new HashMap<>();

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
		product.getProductCategories().forEach(pc -> {
			this.productCategories.put(pc.getId(), pc.getProductCategory());
		});
		product.getProductSubCategories().forEach(pc -> {
			this.productSubCategories.put(pc.getId(), pc.getProductSubCategory());
		});
	}

}