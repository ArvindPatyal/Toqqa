package com.toqqa.service;

import com.toqqa.domain.VerificationStatus;
import com.toqqa.repository.VerificationStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationStatusService {

    @Autowired
    private VerificationStatusRepository statusRepo;

    public void createVerificationStatus(VerificationStatus status){
        log.info("Invoked -+- VerificationStatusService -+- createVerificationStatus()");
        statusRepo.saveAndFlush(status);
    }

    public void deleteVerificationStatus(VerificationStatus status){
        log.info("Invoked -+- VerificationStatusService -+- deleteVerificationStatus()");
        statusRepo.delete(status);
    }
}
