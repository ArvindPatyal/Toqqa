package com.toqqa.bo;

import com.toqqa.domain.SubCategory;

import lombok.Data;

@Data
public class SubCategoryBo {
	private String id;
	private String subCategory;

	public SubCategoryBo(SubCategory subCategory) {
		this.subCategory = subCategory.getSubcategory();
		this.id = subCategory.getId();
	}

}
