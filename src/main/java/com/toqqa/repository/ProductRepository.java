package com.toqqa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Product;
import com.toqqa.domain.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

//	List<Product> findByUserAndIsDeleted(User user, Boolean isDeleted);

	Page<Product> findByUserAndIsDeleted(Pageable pageRequest, User user, Boolean isDeleted);

	Page<Product> findByIsDeleted(Pageable pageRequest, Boolean isDeleted);

	Page<Product> findByProductCategories_IdInAndIsDeleted(Pageable pageRequest, List<String> categoryId,
			Boolean isDeleted);

	Page<Product> findByProductCategories_IdInAndIsDeletedAndUser_Id(Pageable pageRequest, List<String> categoryId,
			Boolean isDeleted, String user_Id);

	@Query(value = "SELECT * FROM Product p WHERE " + "p.Product_name LIKE %:param%"
			+ " Or p.description LIKE %:param%", nativeQuery = true)
	Page<Product> fetchProducts(Pageable pageable, String param);
	

}
