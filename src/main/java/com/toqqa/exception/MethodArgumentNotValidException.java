package com.toqqa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MethodArgumentNotValidException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MethodArgumentNotValidException(String message) {
		super(message);
	}

	public MethodArgumentNotValidException(String message, Throwable cause) {
		super(message, cause);
	}
}
