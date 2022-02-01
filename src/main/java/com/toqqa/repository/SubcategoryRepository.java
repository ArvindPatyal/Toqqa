package com.toqqa.repository;

import com.toqqa.domain.Category;
import com.toqqa.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubCategory,String> {

    SubCategory findBySubcategory(String subcategory);

    SubCategory findByCategory(String category);
}
