package com.toqqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PushNotificationResponseDto {
	
	private int status;
	private String message;

}
