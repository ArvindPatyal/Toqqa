package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
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

//    @Email
    private String email;

//    @NotNull
//    @NotBlank
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
}
