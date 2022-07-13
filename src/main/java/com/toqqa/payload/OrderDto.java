package com.toqqa.payload;

import com.toqqa.constants.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    @NotNull
    private Integer pageNumber = 0;

    private LocalDate endDate = LocalDate.now();

    private LocalDate startDate = endDate.minus(1, ChronoUnit.DAYS);

   // private List<OrderStatus> orderStatus;
}
