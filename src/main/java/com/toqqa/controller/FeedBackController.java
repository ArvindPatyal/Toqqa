package com.toqqa.controller;

import com.toqqa.payload.FeedbackPayload;
import com.toqqa.payload.Response;
import com.toqqa.service.FeedbackService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/feedback")
public class FeedBackController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "add to favourites")
    @PostMapping("/add")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    public Response addFavourite(@RequestBody @Valid FeedbackPayload feedbackPayload) {
        log.info("Invoked:: FavouriteController:: addFavourite");
        return this.feedbackService.addFeedback(feedbackPayload);
    }
}
