package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

}
