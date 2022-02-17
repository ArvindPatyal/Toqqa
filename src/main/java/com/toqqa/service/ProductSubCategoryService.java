package com.toqqa.service;

import java.util.List;

import com.toqqa.bo.ProductSubCategoryBo;

public interface ProductSubCategoryService {

	List<ProductSubCategoryBo> getProductSubCategories(String productcategoryId);

}
