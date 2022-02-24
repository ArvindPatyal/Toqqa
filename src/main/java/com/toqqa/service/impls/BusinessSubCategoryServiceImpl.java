package com.toqqa.service.impls;

import com.toqqa.bo.SubCategoryBo;
import com.toqqa.domain.SubCategory;
import com.toqqa.repository.SubcategoryRepository;
import com.toqqa.service.BusinessSubCategoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BusinessSubCategoryServiceImpl implements BusinessSubCategoryService {
	@Autowired
	private SubcategoryRepository subcategoryRepository;

	@Override
	public List<SubCategoryBo> getSubCategories(String categoryId) {
		log.info("Inside get subcategories");
		List<SubCategory> subCategories = this.subcategoryRepository.findByCategory_id(categoryId);
		List<SubCategoryBo> subCategoryBos = new ArrayList<>();
		subCategories.forEach(subCategory -> subCategoryBos.add(new SubCategoryBo(subCategory)));
		return subCategoryBos;
	}
}
