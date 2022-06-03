package com.toqqa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.toqqa.dto.CustomResponseDto;

@Component
public class BaseController {
	
	public CustomResponseDto doSuccessResponse(Object object, String message) {
		CustomResponseDto response = new CustomResponseDto();
		response.setStatus(HttpStatus.OK);
		response.setData(object);
		response.setMessage(message);
		return response;
	}

	public CustomResponseDto doSuccessResponse(Object object) {
		CustomResponseDto response = doSuccessResponse(object, null);
		return response;
	}

	public CustomResponseDto doErrorResponse(String message) {
		CustomResponseDto response = new CustomResponseDto();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(message);
		response.setHasError(true);
		return response;
	}

	public CustomResponseDto doSuccessResponse(String message) {
		CustomResponseDto response = doSuccessResponse(null, message);
		return response;
	}

}
