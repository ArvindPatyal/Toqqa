package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

	Category findByCategory(String category);
}
