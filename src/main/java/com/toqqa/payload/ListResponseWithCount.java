package com.toqqa.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseWithCount<T> {

	private List<T> data;
	private String message;
	private long totalElements;
	private Integer pageNumber;
	private Integer totalPages;
}
