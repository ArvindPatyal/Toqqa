package com.toqqa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.OrderItem;
import com.toqqa.util.Constants;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
	List<OrderItem> findByOrderInfo(OrderInfo orderInfo);
	
	@Query(value = Constants.TOTAL_ORDER_ITEM_COUNT_QUERY_BY_DATE, nativeQuery= true)
	Optional<Integer> findDeliveredQtyCountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate);

}
