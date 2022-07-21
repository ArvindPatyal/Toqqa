package com.toqqa.dto;

import com.toqqa.constants.NotificationRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationHistoryDto {

    private NotificationRoles notificationFor;
}
