package com.toqqa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
	List<OrderItem> findByOrderInfo(OrderInfo orderInfo);

}
