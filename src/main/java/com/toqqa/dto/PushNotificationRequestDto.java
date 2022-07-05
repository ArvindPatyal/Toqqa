package com.toqqa.dto;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class PushNotificationRequestDto {
	
	private String title;
	private String message;
	private String topic;
	private String token;

}