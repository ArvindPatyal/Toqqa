package com.toqqa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Product;
import com.toqqa.domain.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	Page<Product> findByUserAndIsDeleted(Pageable pageRequest, User user, Boolean isDeleted);

	Page<Product> findByIsDeleted(Pageable pageRequest, Boolean isDeleted);

	Page<Product> findByUser_IdAndIsDeleted(Pageable pageRequest, String userId, Boolean isDeleted);

	Page<Product> findByProductCategories_IdInAndIsDeletedAndUser_Id(Pageable pageRequest, List<String> categoryId,
			Boolean isDeleted, String user_Id);

	Page<Product> findByProductCategories_IdInAndIsDeleted(Pageable pageRequest, List<String> categoryId,
			Boolean isDeleted);

	@Query(value = "SELECT * FROM Product p WHERE " + "p.Product_name LIKE %:param%"
			+ " Or p.description LIKE %:param% And p.is_deleted=:isDeleted", nativeQuery = true)
	Page<Product> fetchProducts(Pageable pageable, String param, @Param("isDeleted") Boolean isDeleted);

	@Query(value = "SELECT * FROM Product p WHERE " + "p.is_deleted=:isDeleted", nativeQuery = true)
	Page<Product> fetchProducts(Pageable pageable, @Param("isDeleted") Boolean isDeleted);

	Page<Product> findByProductCategories_IdAndProductNameContainsOrDescriptionContainsAndIsDeleted(
			Pageable pageRequest, String categoryId, String nameParam, String descParam, Boolean isDeleted);

	Page<Product> findByIsDeletedAndProductNameContainsOrDescriptionContains(Pageable pageRequest, Boolean isDeleted,
			String nameParam, String descParam);

	Page<Product> findByProductCategories_IdAndIsDeleted(Pageable pageRequest, String categoryId, Boolean isDeleted);

	Page<Product> findByProductCategories_IdInAndIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(
			List<String> categoryId, Boolean isDeleted, Pageable pageRequest, Integer minimumUnitsInOneOrder);

	Page<Product> findByIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(Boolean isDeleted, Pageable pageRequest,
			Integer minimumUnitsInOneOrder);

}
