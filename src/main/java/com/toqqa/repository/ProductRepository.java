package com.toqqa.repository;

import com.toqqa.domain.Product;
import com.toqqa.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByUserAndIsDeleted(Pageable pageRequest, User user, Boolean isDeleted);

    Page<Product> findByIsDeleted(Pageable pageRequest, Boolean isDeleted);

    List<Product> findByUser(User user);

    Page<Product> findByUser_IdAndIsDeleted(Pageable pageRequest, String userId, Boolean isDeleted);

    Page<Product> findByProductCategories_IdInAndIsDeletedAndUser_Id(Pageable pageRequest, List<String> categoryId,
                                                                     Boolean isDeleted, String user_Id);

    Page<Product> findByProductCategories_IdInAndIsDeleted(Pageable pageRequest, List<String> categoryId,
                                                           Boolean isDeleted);

    Page<Product> findByProductCategoriesIdInAndProductNameContainsAndIsDeletedOrProductCategoriesIdInAndDescriptionContainsAndIsDeleted(
            Pageable pageRequest, List<String> categoryId, String nameParam, Boolean isDeleted, List<String> categoryIds,String descParam, Boolean deleted);

//    @Query(value = "SELECT * FROM product p Where ")

//    Page<Product> findByProductCategoriesIdInAndProductNameContainsAndIsDeletedOrDescriptionContainsAndIsDeleted(
//            Pageable pageRequest, List<String> categoryId, String nameParam, Boolean isDeleted, String descParam, Boolean deleted);

    Page<Product> findByProductCategories_IdInAndIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(
            List<String> categoryId, Boolean isDeleted, Pageable pageRequest, Integer minimumUnitsInOneOrder);


   /* Page<Product> findByIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(Boolean isDeleted, Pageable pageRequest,
                                                                           Integer minimumUnitsInOneOrder);

    Page<Product> findByIsDeletedAndProductNameContainsOrDescriptionContainsAndIsDeleted(Pageable pageRequest, Boolean isDeleted,
                                                                                         String nameParam, String descParam, Boolean deleted);

    Page<Product> findByProductCategories_IdAndIsDeleted(Pageable pageRequest, String categoryId, Boolean isDeleted);

    @Query(value = "SELECT * FROM Product p WHERE " + "p.Product_name LIKE %:param%"
            + " And p.description LIKE %:param% And p.is_deleted=:isDeleted", nativeQuery = true)
    Page<Product> searchProducts(Pageable pageable, String param, @Param("isDeleted") Boolean isDeleted);

    @Query(value = "SELECT * FROM Product p WHERE " + "p.is_deleted=:isDeleted", nativeQuery = true)
    Page<Product> fetchProducts(Pageable pageable, @Param("isDeleted") Boolean isDeleted);
*/

}
