package com.toqqa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmeStatsResponseDto {
	
	int totalSales;
	int totalProductsSold;
	int totalOrderDelivered;
	int totalOrderCancelled;
	int totalOrderPlaced;
	int totalOrderReceived;
	int totalOrderConfirmed;
	int totalOrderOut;



}
