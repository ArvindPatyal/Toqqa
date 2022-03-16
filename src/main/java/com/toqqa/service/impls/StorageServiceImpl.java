package com.toqqa.service.impls;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Future;

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
    public Resource downloadFile(String fileName) throws IOException {
        String[] name = fileName.split("&");
        byte[] stream = this.fetchFileFromAWS(name[0], name[1], name[2]);
        if (stream != null) {
            ByteArrayResource rsc = new ByteArrayResource(stream);
            return rsc;
        }
        throw new ResourceNotFoundException("no image or file found with name " + fileName);
    }

    private byte[] fetchFileFromAWS(String userId, String dir, String fileName) {
        log.info("Inside fetchFileFromAWS");
        S3Object s3Object = s3Client.getObject(bucketName + "/" + userId + "/" + dir, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content =
                    IOUtils.toByteArray(s3Object.getObjectContent());
            return content;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String buildResourceString(String userId, String folder, String file) {
        return userId + "&" + folder + "&" + file;
    }

    @Override
    public String buildResourceString(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            String[] path = filePath.split("/");
            return path[1] + "/" + path[2] + "/" + path[3];
        }
        return "";
    }
    @Override
    public String generatePresignedUrl(String location) {
        if (location != null && !location.isEmpty()) {
            String pathArr[] = location.split("/");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, 1);
            return s3Client.generatePresignedUrl(bucketName+"/"+pathArr[1]+"/"+pathArr[2],pathArr[3],calendar.getTime()).toString();
        }
        return "";
    }
}
