package com.toqqa.repository;

import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
import com.toqqa.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

//	List<CartItem> findByCart(Cart cart);
//
//	CartItem findByProduct_Id(String id);

	void deleteByCartIdAndProduct(String cartId, Product product);

	CartItem findByProductIdAndCart(String productId, Cart cart);

}
