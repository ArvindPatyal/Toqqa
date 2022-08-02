package com.toqqa.config;

import com.toqqa.exception.UrlNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Value("${admin.secret.key}")
    private String key;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getHeader("SuperUserSecretCode") == null) {
            throw new UrlNotFoundException("URL NOT FOUND");
        }
        String secretCode = request.getHeader("SuperUserSecretCode");
        if (secretCode.equals(key)) {
            return true;
        }
        throw new UrlNotFoundException("URL NOT FOUND");
    }
}
