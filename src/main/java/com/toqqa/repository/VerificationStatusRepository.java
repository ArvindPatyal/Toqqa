package com.toqqa.repository;

import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface VerificationStatusRepository extends JpaRepository<VerificationStatus, String> {

    List<VerificationStatus> findByUser(User user);

    List<VerificationStatus> findByStatusIn(List<VerificationStatusConstants> verificationStatusConstants);

//    List<VerificationStatus> findFirst4ByOrderByCreatedDateAtDescAndStatusIn(ArrayList<VerificationStatusConstants> verificationStatusConstants);
}
