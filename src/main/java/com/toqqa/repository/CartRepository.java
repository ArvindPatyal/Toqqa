package com.toqqa.repository;

import com.toqqa.domain.Cart;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

	Cart findByUser(User user);

	void deleteByUser(User user);
}
