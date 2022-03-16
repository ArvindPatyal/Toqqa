package com.toqqa.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	String uploadFile(MultipartFile file,String userId,String dir);
	
	Future <String> uploadFileAsync(MultipartFile file,String userId,String dir);

	Resource downloadFile(String fileName)throws IOException;
	
//	ByteArrayOutputStream downloadFile(String fileName, String folderName);

	String deleteFile(String fileName);

	File convertMultiPartFileToFile(MultipartFile file);

	String buildResourceString(String userId, String folder, String file);

	String buildResourceString(String filePath);

	String generatePresignedUrl(String location);
}
