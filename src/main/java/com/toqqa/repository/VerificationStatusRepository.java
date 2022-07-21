package com.toqqa.repository;

import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationStatusRepository extends JpaRepository<VerificationStatus,String> {

    List<VerificationStatus> findByUser(User user);

}
