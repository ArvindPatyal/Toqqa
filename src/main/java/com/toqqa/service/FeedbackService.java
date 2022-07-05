package com.toqqa.service;

import com.toqqa.payload.FeedbackPayload;
import com.toqqa.payload.Response;

public interface FeedbackService {
    Response addFeedback(FeedbackPayload feedbackPayload);


}
