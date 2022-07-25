package com.toqqa.dto;

import com.toqqa.constants.NotificationRoles;
import com.toqqa.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private String title;
    private String message;
    private String topic;
    private User user;
    private NotificationRoles role;
}
