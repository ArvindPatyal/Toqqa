package com.toqqa.service.impls;

import com.toqqa.bo.ProductSubCategoryBo;
import com.toqqa.domain.ProductSubCategory;
import com.toqqa.payload.FetchSubCategoriesPayload;
import com.toqqa.repository.ProductSubCategoryRepository;
import com.toqqa.service.ProductSubCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductSubCategoryServiceImpl implements ProductSubCategoryService {

	@Autowired
	private ProductSubCategoryRepository productSubCategoryRepository;

	@Override
	public List<ProductSubCategoryBo> getProductSubCategories(FetchSubCategoriesPayload getSub) {
		log.info("Invoked :: ProductSubCategoryServiceImpl :: getProductSubCategories()");
		List<ProductSubCategory> productSubCategories = this.productSubCategoryRepository
				.findByProductCategory_IdIn(getSub.getCategoryIds());
		List<ProductSubCategoryBo> productSubCategoryBos = new ArrayList<>();
		productSubCategories
				.forEach(productSubCategory -> productSubCategoryBos.add(new ProductSubCategoryBo(productSubCategory)));
		return productSubCategoryBos;
	}

}
