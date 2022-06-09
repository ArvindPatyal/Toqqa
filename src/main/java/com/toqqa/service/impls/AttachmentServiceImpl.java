package com.toqqa.service.impls;

import com.toqqa.domain.Attachment;
import com.toqqa.domain.Product;
import com.toqqa.repository.AttachmentRepository;
import com.toqqa.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepo;

    @Override
    public Attachment addAttachment(String location, String fileType, String fileName, String mimeType, Product product) {
        log.info("Invoked :: AttachmentServiceImpl :: addAttachment()");
        Attachment att = new Attachment();
        att.setFileName(fileName);
        att.setFileType(fileType);
        att.setLocation(location);
        att.setMimeType(mimeType);
        att.setProduct(product);
        att = this.attachmentRepo.saveAndFlush(att);

        return att;
    }

}
