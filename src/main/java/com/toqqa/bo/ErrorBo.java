package com.toqqa.bo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorBo {

	private HttpStatus status;
	private String message;
	private List<String> errors;
	private Long date = new Date().getTime();

	public ErrorBo(String message) {
		super();

		this.message = message;

	}

	public ErrorBo(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		this.message = message;
		this.errors = Arrays.asList(error);

	}
}
