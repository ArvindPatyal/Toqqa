package com.toqqa.repository;

import com.toqqa.domain.Category;
import com.toqqa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {

    Category findByCategory(String category);
}
