package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUp {
    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    private String email;

    @NotBlank
    @NotNull
    private String phone;

    @NotNull
    private String address;

    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private String postCode;

    @NotNull
    @NotBlank
    private String state;

    @NotNull
    @NotBlank
    private String country;

    private String agentId;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    @NotBlank
    private String otp;
    @NotNull
    @NotBlank
    private String loginId;


}
