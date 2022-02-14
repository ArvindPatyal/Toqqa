package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	Role findByRole(String role);
}
