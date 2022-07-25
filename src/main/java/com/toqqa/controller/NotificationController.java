package com.toqqa.controller;

import com.toqqa.dto.NotificationHistoryDto;
import com.toqqa.payload.Response;
import com.toqqa.service.impls.PushNotificationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @ApiOperation(value = "Notification")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/listNotification")
    public Response listNotification(@RequestBody @Valid NotificationHistoryDto notificationHistoryDto) {
        log.info("Invoked:: NotificationController:: listNotification");
        return this.pushNotificationService.notifications(notificationHistoryDto);

    }
}
