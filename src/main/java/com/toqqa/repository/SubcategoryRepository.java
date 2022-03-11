package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.SubCategory;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubCategory, String> {

	SubCategory findBySubcategory(String subcategory);

	List<SubCategory> findByCategory_IdIn(List<String> ids);
}
