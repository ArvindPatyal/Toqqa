package com.toqqa.repository;

import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByPhone(String phone);
    User findByEmail(String email);

    User findByEmailOrPhone(String email,String phone);

}
