package com.toqqa.service;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderItemService {
	
	Optional<Integer> getDeleviredQtyCountBySmeAndDate(String smeId, LocalDate startDate, LocalDate endDate);

}
