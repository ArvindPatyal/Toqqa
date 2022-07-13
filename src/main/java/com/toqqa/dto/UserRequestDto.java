package com.toqqa.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;

@Getter
@Setter

public class UserRequestDto {
    @NotNull
    private Integer pageNumber = 0;
    @NotNull
    private Sort.Direction sortOrder;
}
