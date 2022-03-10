package com.toqqa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

	List<CartItem> findByCart(Cart cart);
	
	CartItem findByProduct_Id(String id);
}
