package com.toqqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpDto {
    @NotBlank
    private String mobileNumber;
    @NotBlank
    private String countryCode;
//    private String email;


}
