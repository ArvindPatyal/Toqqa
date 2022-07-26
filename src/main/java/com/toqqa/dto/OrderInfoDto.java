package com.toqqa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderInfoDto {

    private List<String> orderStatus;

    private Date endDate;

    private Date startDate;

    private boolean showCancelledOrders = false;
}
