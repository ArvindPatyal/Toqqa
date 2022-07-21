package com.toqqa.service.impls;

import com.toqqa.domain.Feedback;
import com.toqqa.domain.User;
import com.toqqa.dto.EmailRequestDto;
import com.toqqa.payload.FeedbackPayload;
import com.toqqa.payload.Response;
import com.toqqa.repository.FeedbackRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.EmailService;
import com.toqqa.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)

public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Response addFeedback(FeedbackPayload feedbackPayload) {
        log.info("Invoked :: FeedbackServiceImpl :: addFeedback()");
        User user = this.authenticationService.currentUser();
        Feedback feedback = new Feedback();
        feedback.setTitle(feedbackPayload.getTitle());
        feedback.setDescription(feedbackPayload.getDescription());
        feedback.setCreatedDate(new Date());
        feedback.setUser(user);
        feedback = this.feedbackRepository.saveAndFlush(feedback);
        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setSubject(feedbackPayload.getTitle());
        emailRequestDto.setBody(feedbackPayload.getDescription());
        emailRequestDto.setOrder(false);
        try {
            this.emailService.sendEmail(emailRequestDto);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Response<>(feedbackPayload, "Feedback updated successfully");
    }
}