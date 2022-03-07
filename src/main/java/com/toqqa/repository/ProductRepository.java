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
	
		List<Product> findByUser(User user);
		
		Page<Product> findByUser(Pageable pageRequest,User user);
}
