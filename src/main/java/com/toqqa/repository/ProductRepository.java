package com.toqqa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Product;
import com.toqqa.domain.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	List<Product> findByUserAndIsDeleted(User user, Boolean isDeleted);

	Page<Product> findByUserAndIsDeleted(Pageable pageRequest, User user, Boolean isDeleted);

	Page<Product> findByIsDeleted(Pageable pageRequest, Boolean isDeleted);

	//List<Product> findByUserAndIsActiveAndIsDeleted(User user, Boolean isActive, Boolean isDeleted);

}
