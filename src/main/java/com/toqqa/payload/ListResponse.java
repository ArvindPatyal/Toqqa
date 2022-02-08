package com.toqqa.payload;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ListResponse<T> {

	private List<T> data;
	private String message;

	public ListResponse(List<T> d, String msg) {
		this.data = d;
		this.message = msg;
	}
}
