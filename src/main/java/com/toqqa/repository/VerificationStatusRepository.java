package com.toqqa.repository;

import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.util.AdminConstants;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VerificationStatusRepository extends JpaRepository<VerificationStatus, String> {

    List<VerificationStatus> findByUser(User user);

    List<VerificationStatus> findByUserIn(List<User> users);

    List<VerificationStatus> findByUserInAndStatusIn(List<User> users, List<VerificationStatusConstants> verificationStatusConstants);

    List<VerificationStatus> findByRoleIn(Sort sort, List<RoleConstants> roleConstants);

    @Query(value = AdminConstants.TOP_4_NEW_APPROVAL_REQUEST, nativeQuery = true)
    List<VerificationStatus> findFirst4NewRequest();

    @Query(value = AdminConstants.ALL_NEW_CUSTOMER, nativeQuery = true)
    List<VerificationStatus> findByCustomerRolesAndStatus(LocalDate startDate, LocalDate endDate);

    @Query(value = AdminConstants.ALL_NEW_SMES, nativeQuery = true)
    List<VerificationStatus> findBySmeRolesAndStatus(LocalDate startDate, LocalDate endDate);

    @Query(value = AdminConstants.ALL_NEW_AGENTS, nativeQuery = true)
    List<VerificationStatus> findByAgentRolesAndStatus(LocalDate startDate, LocalDate endDate);

}
