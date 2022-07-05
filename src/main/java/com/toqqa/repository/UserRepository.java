package com.toqqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByPhone(String phone);

	User findByEmail(String email);

	User findByEmailOrPhone(String email, String phone);

	List<User> findByAgentId(String agentId);
	
	Optional<User> findById(String id);

}
