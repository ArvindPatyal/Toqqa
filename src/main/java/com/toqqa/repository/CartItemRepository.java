package com.toqqa.repository;

import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

//	List<CartItem> findByCart(Cart cart);
//
//	CartItem findByProduct_Id(String id);

	void deleteByCartAndProductId(Cart cart, String productId);

	CartItem findByProductIdAndCart(String productId, Cart cart);

}
