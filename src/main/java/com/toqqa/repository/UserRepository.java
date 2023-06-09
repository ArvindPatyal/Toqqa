package com.toqqa.repository;

import com.toqqa.domain.Role;
import com.toqqa.domain.User;
import com.toqqa.util.AdminConstants;
import com.toqqa.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByPhone(String phone);

    User findByEmail(String email);

    User findByEmailOrPhone(String email, String phone);

    List<User> findByAgentIdAndRolesIn(String agentId, List<Role> roles);

    Optional<User> findById(String id);

    @Query(value = AdminConstants.TOTAL_USERS_COUNT_QUERY_BY_DATE, nativeQuery = true)
    List<User> findByCreatedDate(LocalDate startDate, LocalDate endDate);

    List<User> findFirst4ByOrderByCreatedAtDesc();

    List<User> findAllByOrderByCreatedAtDesc();

    @Query(value = Constants.USER_SEARCH, nativeQuery = true)
    Page<User> searchUsers(Pageable pageable, String param, @Param("isDeleted") Boolean isDeleted);

    Page<User> findByRolesIn(Pageable pageable, List<Role> roles);

}
