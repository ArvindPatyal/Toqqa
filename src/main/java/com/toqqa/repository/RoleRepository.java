package com.toqqa.repository;

import com.toqqa.domain.Role;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {

    Role findByRole(String role);
}
