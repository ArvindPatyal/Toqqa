package com.toqqa.service;

import com.toqqa.bo.SubCategoryBo;
import com.toqqa.payload.FetchSubCategoriesPayload;

import java.util.List;

public interface BusinessSubCategoryService {
	List<SubCategoryBo> getSubCategories(FetchSubCategoriesPayload getSub);
}
