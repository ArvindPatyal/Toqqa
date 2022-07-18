package com.toqqa.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatsBo {

    private Double sales;

    private Long newUsersRegistered;

    private Long ordersPlaced;
}
