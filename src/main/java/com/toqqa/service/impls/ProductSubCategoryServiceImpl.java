package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.bo.ProductSubCategoryBo;
import com.toqqa.domain.ProductSubCategory;
import com.toqqa.repository.ProductSubCategoryRepository;
import com.toqqa.service.ProductSubCategoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductSubCategoryServiceImpl implements ProductSubCategoryService {

	@Autowired
	private ProductSubCategoryRepository productSubCategoryRepository;

	@Override
	public List<ProductSubCategoryBo> getProductSubCategories(String productcategoryId) {
		log.info("Inside get product sub categories");
		List<ProductSubCategory> productSubCategories = this.productSubCategoryRepository
				.findByProductCategory_id(productcategoryId);
		List<ProductSubCategoryBo> productSubCategoryBos = new ArrayList<>();
		productSubCategories
				.forEach(productSubCategory -> productSubCategoryBos.add(new ProductSubCategoryBo(productSubCategory)));
		return productSubCategoryBos;
	}

}
