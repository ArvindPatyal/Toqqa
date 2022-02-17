package com.toqqa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.ProductSubCategory;

@Repository
public interface ProductSubCategoryRepository extends JpaRepository<ProductSubCategory, String> {

	ProductSubCategory findByProductSubCategory(String productSubCategory);

	List<ProductSubCategory> findByProductCategory_id(String productcategoryId);

}
