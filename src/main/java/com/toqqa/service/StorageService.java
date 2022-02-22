package com.toqqa.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	String uploadFile(MultipartFile file);

	byte[] downloadFile(String fileName);

	String deleteFile(String fileName);

	File convertMultiPartFileToFile(MultipartFile file);

	String createFolder(String folder);

}
