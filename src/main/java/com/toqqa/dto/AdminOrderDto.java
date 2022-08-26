package com.toqqa.dto;

import com.toqqa.constants.OrderStatus;
import com.toqqa.constants.RoleConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminOrderDto {
    private int pageNumber;
    private String sortOrder;
    private String sortKey;
    private List<OrderStatus> status;
}
