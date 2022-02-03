package com.toqqa.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
