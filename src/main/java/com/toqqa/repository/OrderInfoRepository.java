package com.toqqa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.User;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String> {

	List<OrderInfo> findByUser(User user);

	Page<OrderInfo> findByUser(Pageable pageRequest,User user);
	
	

}
