package com.toqqa.service.impls;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.repository.OrderItemRepository;
import com.toqqa.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService{
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	public Optional<Integer> getDeleviredQtyCountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate){
		return orderItemRepository.findDeliveredQtyCountBySmeAndDate(smeId, startDate, endDate);

	}


}
