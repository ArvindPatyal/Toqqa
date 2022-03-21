package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AdvertisementUpdate {

	@NotNull
	private String id;

	@NotNull
	private MultipartFile banner;
    
	@NotNull
	private String productId;

	@NotNull
	private String description;

}
