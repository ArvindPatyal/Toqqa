package com.toqqa.controller;

import com.toqqa.bo.UserBo;
import com.toqqa.payload.UserSignUp;
import com.toqqa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public UserBo addUser(@RequestBody @Valid UserSignUp userSignUp){
        return this.userService.addUser(userSignUp);
    }
}
