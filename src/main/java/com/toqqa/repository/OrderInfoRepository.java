package com.toqqa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toqqa.constants.OrderConstants;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.User;
import com.toqqa.util.Constants;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String> {

	List<OrderInfo> findByUser(User user);

	Page<OrderInfo> findByUser(Pageable pageRequest, User user);

	Page<OrderInfo> findBySmeIdAndOrderStatusIn(Pageable pageRequest, String smeId, List<OrderConstants> orderStatus);

	OrderInfo findByIdAndUser(String id, User user);

	List<OrderInfo> findByUser_IdAndOrderStatus(String userId,OrderConstants orderStatus);
	
	@Query(value = Constants.TOTAL_ORDER_AMOUNT_DELIVERED_QUERY_BY_DATE ,nativeQuery = true)
	Optional<Integer> findDelOrderAmountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate);
	
	@Query(value = Constants.ORDER_COUNT_QUERY_BY_DATE_AND_STATUS, nativeQuery = true)
	Optional<Integer> findOrderCountBySmeAndDateAndStatus(String smeId, String orderStatus, LocalDate startDate, LocalDate endDate);


}
