package com.toqqa.service.impls;

import com.toqqa.bo.ProductCategoryBo;
import com.toqqa.domain.ProductCategory;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Override
	public List<ProductCategoryBo> getProductCategories() {
		log.info("Invoked ::ProductCategoryServiceImpl :: getProductCategories()");
		List<ProductCategory> categories = this.productCategoryRepository.findAll();
		List<ProductCategoryBo> bo = new ArrayList<>();
		categories.forEach(productcategory -> bo.add(new ProductCategoryBo(productcategory)));
		return bo;
	}
}
