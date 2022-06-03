package com.toqqa.dto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponseDto {
	private String message;
 	private HttpStatus status;
	private List<String> errors = new ArrayList<>(0);
	private boolean hasError;	
	private boolean warning;
	private Object data;
	
	public CustomResponseDto() {}
	
	public CustomResponseDto(List<String> errors, boolean hasError,Object data) {
		super();
		this.errors = errors;
		this.hasError = hasError;
		this.data = data;
	}

}
