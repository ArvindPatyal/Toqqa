package com.toqqa.bo;

import com.toqqa.domain.ProductSubCategory;

import lombok.Data;

@Data
public class ProductSubCategoryBo {

	private String id;
	private String productSubCategory;

	public ProductSubCategoryBo(ProductSubCategory productSubCategory) {
		this.id = productSubCategory.getId();
		this.productSubCategory=productSubCategory.getProductSubCategory();

	}

}
