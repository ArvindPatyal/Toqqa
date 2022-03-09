package com.toqqa.service;

import com.toqqa.domain.Attachment;

public interface AttachmentService {
	
	Attachment addAttachment(String location,String fileType,String fileName,String mimeType);
	
}
