package com.toqqa.repository;

import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.util.AdminConstants;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationStatusRepository extends JpaRepository<VerificationStatus, String> {

    List<VerificationStatus> findByUser(User user);

    List<VerificationStatus> findByStatusIn(Sort sort, List<VerificationStatusConstants> verificationStatusConstants);

    List<VerificationStatus> findFirst4ByOrderByCreatedDateDesc();

    @Query(value = AdminConstants.TOP_4_NEW_APPROVAL_REQUEST, nativeQuery = true)
    List<VerificationStatus> findFirst4NewRequest();

}
