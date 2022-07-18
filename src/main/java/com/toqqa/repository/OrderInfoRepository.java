package com.toqqa.repository;

import com.toqqa.constants.OrderStatus;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.User;
import com.toqqa.util.AdminConstants;
import com.toqqa.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String> {

    Page<OrderInfo> findByUser(Pageable pageRequest, User user);

    Page<OrderInfo> findBySmeIdAndOrderStatusIn(Pageable pageRequest, String smeId, List<OrderStatus> orderStatus);

    List<OrderInfo> findByUser_IdAndOrderStatus(String userId, OrderStatus orderStatus);

    List<OrderInfo> findFirst4ByOrderByCreatedDateDesc();

    @Query(value = Constants.TOTAL_ORDER_AMOUNT_DELIVERED_QUERY_BY_DATE, nativeQuery = true)
    Optional<Integer> findDelOrderAmountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate);

    @Query(value = Constants.ORDER_COUNT_QUERY_BY_DATE_AND_STATUS, nativeQuery = true)
    Optional<Integer> findOrderCountBySmeAndDateAndStatus(String smeId, String orderStatus, LocalDate startDate, LocalDate endDate);

    @Query(value = AdminConstants.TOTAL_ORDERS_COUNT_QUERY_BY_DATE, nativeQuery = true)
    Page<OrderInfo> findByModificationDate(Pageable pageRequest, LocalDate startDate, LocalDate endDate);

    @Query(value = AdminConstants.TOTAL_ORDERS_COUNT_QUERY_BY_DATE, nativeQuery = true)
    List<OrderInfo> findByCreatedDate(LocalDate startDate, LocalDate endDate);

    @Query(value = AdminConstants.TOTAL_ORDER_AMOUNT_DELIVERED_BY_DATE, nativeQuery = true)
    Optional<Double> findTotalAmountByDate(LocalDate startDate, LocalDate endDate);


}
