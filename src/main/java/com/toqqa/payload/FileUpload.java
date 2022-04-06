package com.toqqa.payload;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FileUpload {

	private String id;

	private MultipartFile file;
}
