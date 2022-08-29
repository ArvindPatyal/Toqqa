package com.toqqa.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatsBo {

    private Long newOrders;

    private Long cancelledOrders;

    private Long deliveredOrders;

}
