package com.toqqa.service;

import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.repository.VerificationStatusRepository;
import com.toqqa.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VerificationStatusService {

    @Autowired
    private VerificationStatusRepository statusRepo;
    @Autowired
    private AuthenticationService authenticationService;

    public void createVerificationStatus(VerificationStatus status) {
        log.info("Invoked -+- VerificationStatusService -+- createVerificationStatus()");
        statusRepo.saveAndFlush(status);
    }

    public void deleteVerificationStatus(VerificationStatus status) {
        log.info("Invoked -+- VerificationStatusService -+- deleteVerificationStatus()");
        statusRepo.delete(status);
    }

    public void updateVerificationStatus(String statusId, VerificationStatusConstants status) {
        log.info("Invoked -+- VerificationStatusService -+- updateVerificationStatus()");
        VerificationStatus verificationStatus = statusRepo.findById(statusId).orElseThrow(() -> new ResourceNotFoundException(Constants.NO_VERIFICATION_STATUS_FOUND));
        verificationStatus.setStatus(status);
        this.statusRepo.saveAndFlush(verificationStatus);
    }

    public Map<String, String> verificationStatusOfAUser() {
        log.info("Invoked -+- VerificationStatusService -+- verificationStatusOfAUser()");
        List<VerificationStatus> verificationStatuses = this.statusRepo.findByUser(this.authenticationService.currentUser());
        Map<String, String> verificationStatusMap = new HashMap<>();
        verificationStatuses.forEach(verificationStatus -> verificationStatusMap.put(verificationStatus.getRole().getValue(), verificationStatus.getStatus().toString()));
        return verificationStatusMap;

    }
}
