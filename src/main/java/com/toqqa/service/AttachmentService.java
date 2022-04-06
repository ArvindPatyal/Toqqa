package com.toqqa.service;

import com.toqqa.domain.Attachment;
import com.toqqa.domain.Product;

public interface AttachmentService {
	
	Attachment addAttachment(String location,String fileType,String fileName,String mimeType,Product product);
	
}
