package com.toqqa.repository;

import com.toqqa.domain.Cart;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByPhone(String phone);

	User findByEmail(String email);

	User findByEmailOrPhone(String email, String phone);

	List<User> findByAgentId(String agentId);

}
