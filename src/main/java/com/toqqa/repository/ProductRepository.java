package com.toqqa.repository;

import com.toqqa.domain.Product;
import com.toqqa.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	List<Product> findByUserAndIsDeleted(User user, Boolean isDeleted);

	Page<Product> findByUserAndIsDeleted(Pageable pageRequest, User user, Boolean isDeleted);

	Page<Product> findByIsDeleted(Pageable pageRequest, Boolean isDeleted);

}
