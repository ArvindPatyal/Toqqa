package com.toqqa.service;

import com.toqqa.bo.SubCategoryBo;

import java.util.List;

public interface BusinessSubCategoryService {
    List<SubCategoryBo> getSubCategories(String categoryId);
}
