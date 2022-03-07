package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.OrderInfo;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String> {
	

}
