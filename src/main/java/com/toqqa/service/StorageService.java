package com.toqqa.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Future;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	String uploadFile(MultipartFile file,String userId,String dir);
	
	Future <String> uploadFileAsync(MultipartFile file,String userId,String dir);

	//byte[] downloadFile(String fileName);
	
	ByteArrayOutputStream downloadFile(String fileName, String folderName);

	String deleteFile(String fileName);

	File convertMultiPartFileToFile(MultipartFile file);

	String createFolder(String folder);

}
