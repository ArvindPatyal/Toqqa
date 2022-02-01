package com.toqqa.service;

import com.toqqa.bo.UserBo;
import com.toqqa.payload.UserSignUp;

public interface UserService {
    UserBo addUser(UserSignUp userSignUp);

    Boolean isUserExists(String email, String phone);
}
