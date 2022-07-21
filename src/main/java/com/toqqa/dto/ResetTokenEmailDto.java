package com.toqqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetTokenEmailDto {
    private String mailTo;
    private String from;
    private String mailSubject;
    private String mailContent;
    private String userName;
    private String tokenUrl;

}
