package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@Size(max = 255)
    private String email;

    @NotBlank
    @NotNull
    private String phone;

    @NotNull
    @Size(max = 255)
    private String address;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String city;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String postCode;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String state;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String country;

    private String agentId;

    @NotNull
    @NotBlank
    @Size(max = 255)
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
    @Size(max = 255)
    private String loginId;


}
