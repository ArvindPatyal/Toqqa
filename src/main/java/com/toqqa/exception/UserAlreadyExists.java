package com.toqqa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExists extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	
	public UserAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

	public UserAlreadyExists(String msg) {
		super(msg);
	}

}
