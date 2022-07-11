package com.toqqa.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;

@Getter
@Setter
public class UsersDto {

    @NotNull
    private Integer pageNumber = 0;

    private LocalDate endDate = LocalDate.now();

    private LocalDate startDate = endDate.minus(1, ChronoUnit.MONTHS);
}