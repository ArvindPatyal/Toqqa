package com.toqqa.config;

import com.toqqa.exception.BadRequestException;
import com.toqqa.util.Helper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class CustomerInterceptor implements HandlerInterceptor {

    private final Helper helper;


    public CustomerInterceptor(Helper helper) {
        this.helper = helper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userType = request.getHeader("UserType");
        if (!helper.notNullAndBlank(userType)) {
            throw new BadRequestException("header not found!! Please add UserType header!!");
        }

        if (userType.equals("ROLE_CUSTOMER")) {
            throw new AccessDeniedException("Access denied");
        } else {
            return true;
        }
    }

}
