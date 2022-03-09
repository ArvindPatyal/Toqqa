package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Cart;
import com.toqqa.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
	
	Cart findByUser(User user);
}
