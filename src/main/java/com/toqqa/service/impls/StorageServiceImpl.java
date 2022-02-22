package com.toqqa.service.impls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.waiters.AmazonS3Waiters;
import com.amazonaws.util.IOUtils;
import com.toqqa.service.StorageService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

	@Value("${bucketName}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3Client;

	public String uploadFile(MultipartFile file) {
		File fileObj = convertMultiPartFileToFile(file);
		String fileName = System.currentTimeMillis() + " " + file.getOriginalFilename();
		s3Client.putObject(new PutObjectRequest(bucketName+"/test/", fileName, fileObj));
		fileObj.delete();
		return "File Uploaded :" + fileName;
	}

	public byte[] downloadFile(String fileName) {
		S3Object s3Object = s3Client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public String deleteFile(String fileName) {
		s3Client.deleteObject(bucketName, fileName);
		return fileName + "removed...";
	}

	public File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());

		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {

			fos.write(file.getBytes());

		} catch (IOException e) {

			log.error("Error converting multipartFile to file", e);
		}

		return convertedFile;
	}

	@Override
	public String createFolder(String folder) {

		/*
		 * TransferManager xfer_mgr = new TransferManager();
		 * 
		 * String dir_path = "D: /workspace/test";
		 * 
		 * String key_prefix = "";
		 * 
		 * String bucket_name = "toqqa_dev";
		 * 
		 * boolean recursive = false;
		 * 
		 * MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucket_name, key_prefix,
		 * new File(dir_path), recursive);
		 * 
		 * try {
		 * 
		 * xfer.waitForCompletion(); } catch (AmazonServiceException e) {
		 * 
		 * System.err.println("Amazon service error:" + e.getMessage()); System.exit(1);
		 * }
		 * 
		 * catch (AmazonClientException e) { System.err.println("Amazon client error:" +
		 * e.getMessage()); System.exit(1);
		 * 
		 * } catch (InterruptedException e) {
		 * 
		 * System.err.println("Transfer interrupted:" + e.getMessage()); System.exit(1);
		 * 
		 * }
		 * 
		 * xfer_mgr.shutdownNow();
		 * 
		 * return null;
		 */

		String bucketName = "nam-public-images";
		String folderName = "asia/vietnam/";

		//PutObjectRequest request =new PutObjectRequest(bucketName, folderName);
		/*
		 * s3Client.putObject(new PutObjectRequest(bucketName, folderName,));
		 * 
		 * AmazonS3Waiters waiter = s3Client.waiters(); HeadObjectRequest requestWait =
		 * HeadObjectRequest.builder().bucket(bucketName).key(folderName).build();
		 * 
		 * WaiterResponse<HeadObjectResponse> waiterResponse =
		 * waiter.waitUntilObjectExists(requestWait);
		 * 
		 * waiterResponse.matched().response().ifPresent(System.out::println);
		 * 
		 * System.out.println("Folder " + folderName + " is ready.");
		 */
		return "";
	}
}
