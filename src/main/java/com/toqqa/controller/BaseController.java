package com.toqqa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.toqqa.dto.CustomResponseDto;
import com.toqqa.payload.ListResponse;

@Component
public class BaseController {
	
	public <T> ListResponse<T> doSuccessResponse(List<T> object, String message) {
		ListResponse<T> response = new ListResponse<>(object,message);
		return response;
	}

	public CustomResponseDto doErrorResponse(String message) {
		CustomResponseDto response = new CustomResponseDto();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(message);
		response.setHasError(true);
		return response;
	}


}
