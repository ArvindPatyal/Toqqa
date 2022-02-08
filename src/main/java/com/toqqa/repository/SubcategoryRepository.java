package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.SubCategory;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubCategory, String> {

	SubCategory findBySubcategory(String subcategory);

	SubCategory findByCategory(String category);
}
