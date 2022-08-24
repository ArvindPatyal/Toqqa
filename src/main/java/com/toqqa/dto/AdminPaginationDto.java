package com.toqqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminPaginationDto<T> {
    private T content;
    private long totalElements;
    private Integer pageNumber;
    private Integer totalPages;
}
