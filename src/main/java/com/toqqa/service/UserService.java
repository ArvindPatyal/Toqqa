package com.toqqa.service;

import com.toqqa.bo.UserBO;
import com.toqqa.payload.UserSignUp;

public interface UserService {
    UserBO addUser(UserSignUp userSignUp);

    Boolean isUserExists(String email, String phone);
}
