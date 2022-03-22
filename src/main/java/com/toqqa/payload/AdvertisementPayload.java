package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AdvertisementPayload {

	@NotNull
	private String description;

	@NotNull
	private MultipartFile banner;

	@NotNull
	private String productId;


}
