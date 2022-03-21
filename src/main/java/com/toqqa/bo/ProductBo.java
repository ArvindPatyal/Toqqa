package com.toqqa.bo;

import java.util.*;

import com.toqqa.domain.Product;

import lombok.Data;

@Data
public class ProductBo {

	private String id;

	private String productName;

	private List<ProductCategoryBo> productCategories = new ArrayList<ProductCategoryBo>();

	private List<ProductSubCategoryBo> productSubCategories =new ArrayList<ProductSubCategoryBo>();

	private List<String> images=new ArrayList<>();

	private String description;

//	private String details;

	private Long unitsInStock;

	private Double pricePerUnit;

	private Double discount;

	private Integer maximumUitsInOneOrder;

	private Integer minimumUnitsInOneOrder;

	private Date expiryDate;

	private String countryOfOrigin;

	private String manufacturerName;
	
	private Boolean isDeleted;
	
	private Date ManufacturingDate;
	
	private String banner;

// To Do Awaiting Feedback Around These Two Fields..

//	private Boolean doYouDelieverInSpecifiedRadius;

//	private Boolean delieveredOutsideOfDelieveryRadius;

	public ProductBo(Product product) {
		this.id = product.getId();
		this.productName = product.getProductName();
		this.description = product.getDescription();
		this.unitsInStock = product.getUnitsInStock();
		this.pricePerUnit = product.getPricePerUnit();
		this.discount = product.getDiscount();
		this.maximumUitsInOneOrder = product.getMaximumUitsInOneOrder();
		this.minimumUnitsInOneOrder = product.getMinimumUnitsInOneOrder();
		this.expiryDate = product.getExpiryDate();
		this.countryOfOrigin = product.getCountryOfOrigin();
		this.manufacturerName = product.getManufacturerName();
		this.isDeleted = product.getIsDeleted();
		this.ManufacturingDate = product.getManufacturingDate();	
		this.banner = product.getBanner();
		product.getProductCategories().forEach(pc -> {
			this.productCategories.add(new ProductCategoryBo(pc));
		});
		product.getProductSubCategories().forEach(pc -> {
			this.productSubCategories.add(new ProductSubCategoryBo(pc));
		});

	}
	public ProductBo(Product product,List<String> images) {
		this.id = product.getId();
		this.productName = product.getProductName();
		this.description = product.getDescription();
		this.unitsInStock = product.getUnitsInStock();
		this.pricePerUnit = product.getPricePerUnit();
		this.discount = product.getDiscount();
		this.maximumUitsInOneOrder = product.getMaximumUitsInOneOrder();
		this.minimumUnitsInOneOrder = product.getMinimumUnitsInOneOrder();
		this.expiryDate = product.getExpiryDate();
		this.countryOfOrigin = product.getCountryOfOrigin();
		this.manufacturerName = product.getManufacturerName();
		this.isDeleted = product.getIsDeleted();
		this.banner = product.getBanner();
		product.getProductCategories().forEach(pc -> {
			this.productCategories.add(new ProductCategoryBo(pc));
		});
		product.getProductSubCategories().forEach(pc -> {
			this.productSubCategories.add(new ProductSubCategoryBo(pc));
		});
		this.images.addAll(images);
		this.ManufacturingDate = product.getManufacturingDate();
	}

}
