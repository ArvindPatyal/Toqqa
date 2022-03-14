package com.toqqa.service;

import java.util.List;

import com.toqqa.bo.ProductSubCategoryBo;
import com.toqqa.payload.FetchSubCategoriesPayload;

public interface ProductSubCategoryService {

	List<ProductSubCategoryBo> getProductSubCategories(FetchSubCategoriesPayload getSub);

}
