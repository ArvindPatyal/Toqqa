package com.toqqa.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.toqqa.util.AdminConstants;
import com.toqqa.util.Constants;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import com.toqqa.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByPhone(String phone);

	User findByEmail(String email);

	User findByEmailOrPhone(String email, String phone);

	List<User> findByAgentId(String agentId);
	
	Optional<User> findById(String id);
	@Query(value = AdminConstants.TOTAL_USERS_COUNT_QUERY_BY_DATE, nativeQuery = true)
	Page<User> findByCreatedDate(Pageable pageRequest, LocalDate startDate, LocalDate endDate);
}
