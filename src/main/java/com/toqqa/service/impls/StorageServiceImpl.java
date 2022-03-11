package com.toqqa.service.impls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.toqqa.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

	@Value("${bucketName}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3Client;

	@Override
	@Async
	public Future<String> uploadFileAsync(MultipartFile file, String userId, String dir) {
		log.info("Inside file upload async");
		String folderName = bucketName + "/" + userId + "/" + dir;
		File fileObj = convertMultiPartFileToFile(file);
		String fileName = System.currentTimeMillis() + "" + file.getOriginalFilename();
		s3Client.putObject(new PutObjectRequest(folderName, fileName, fileObj));
		fileObj.delete();
		return new AsyncResult<String>(folderName + "/" + fileName);
	}

	@Async
	public String uploadFile(MultipartFile file, String userId, String dir) {
		log.info("Inside file upload");
		File fileObj = convertMultiPartFileToFile(file);
		String fileName = System.currentTimeMillis() + " " + file.getOriginalFilename();
		s3Client.putObject(new PutObjectRequest(bucketName + "/" + userId + "/" + dir, fileName, fileObj));
		fileObj.delete();
		return fileName;
	}

	public ByteArrayOutputStream downloadFile(String fileName, String bucketDir) {
		try {
			S3Object s3object = s3Client.getObject(bucketName + "/" + bucketDir, fileName);

			InputStream is = s3object.getObjectContent();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, len);
			}

			return outputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public byte[] downloadFile(String fileName) {
	 * log.info("Inside file download"); S3Object s3Object =
	 * s3Client.getObject(bucketName, fileName); S3ObjectInputStream inputStream =
	 * s3Object.getObjectContent(); try { byte[] content =
	 * IOUtils.toByteArray(inputStream); return content; } catch (IOException e) {
	 * 
	 * e.printStackTrace(); } return null; }
	 */

	public String deleteFile(String fileName) {
		log.info("Inside delete file");
		s3Client.deleteObject(bucketName, fileName);
		return fileName + "removed...";
	}

	public File convertMultiPartFileToFile(MultipartFile file) {

		log.info("Inside Convert multipart to file");
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

		/*
		 * String bucketName = "nam-public-images"; String folderName = "asia/vietnam/";
		 */
		// PutObjectRequest request =new PutObjectRequest(bucketName, folderName);
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
