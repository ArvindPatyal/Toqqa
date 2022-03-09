package com.toqqa.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.domain.Attachment;
import com.toqqa.repository.AttachmentRepository;
import com.toqqa.service.AttachmentService;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepo;

	@Override
	public Attachment addAttachment(String location, String fileType, String fileName, String mimeType) {
		Attachment att = new Attachment();
		att.setFileName(fileName);
		att.setFileType(fileType);
		att.setLocation(location);
		att.setMimeType(mimeType);

		att = this.attachmentRepo.saveAndFlush(att);

		return att;
	}

}
