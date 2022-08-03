package com.toqqa.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toqqa.domain.NotificationHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationHistoryBo {

    private String title;
    private String message;
    private String topic;
    private String role;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public NotificationHistoryBo(NotificationHistory notificationHistory) {

        this.title = notificationHistory.getTitle();
        this.message = notificationHistory.getMessage();
        this.topic = notificationHistory.getTopic();
        this.role = String.valueOf(notificationHistory.getRole());
        this.createdDate = notificationHistory.getCreatedDate();
    }
}
