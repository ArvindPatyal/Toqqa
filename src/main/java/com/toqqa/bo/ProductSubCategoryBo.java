package com.toqqa.bo;

import com.toqqa.domain.ProductSubCategory;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSubCategoryBo {

	private String id;
	private String productSubCategory;

	public ProductSubCategoryBo(ProductSubCategory productSubCategory) {
		this.id = productSubCategory.getId();
		this.productSubCategory=productSubCategory.getProductSubCategory();

	}

}
