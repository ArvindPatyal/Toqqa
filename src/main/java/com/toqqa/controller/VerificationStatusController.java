package com.toqqa.controller;

import com.toqqa.payload.Response;
import com.toqqa.service.VerificationStatusService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "api/verification")
public class VerificationStatusController {
    private final VerificationStatusService verificationStatusService;

    @Autowired
    public VerificationStatusController(VerificationStatusService verificationStatusService) {
        this.verificationStatusService = verificationStatusService;
    }

    @ApiOperation(value = "Returns verificationStatus of a user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping()
    public Response VerificationStatusOfAUser() {
        log.info("Invoked:: UserController:: fetchUser");
        return new Response(this.verificationStatusService.verificationStatusOfAUser(), "success");
    }
}
