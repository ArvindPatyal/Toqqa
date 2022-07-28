package com.toqqa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private final Interceptor interceptor;
    private final CustomerInterceptor customerInterceptor;

    @Autowired
    public WebMvcConfig(Interceptor interceptor,
                        CustomerInterceptor customerInterceptor) {
        this.interceptor = interceptor;
        this.customerInterceptor = customerInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE").maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(Arrays.asList("/api/auth/**", "/api/otp", "/api/registration/**","/api/admin/**"));
        registry.addInterceptor(customerInterceptor).addPathPatterns(Arrays.asList("/api/advertisement/**", "/api/agent/**", "/api/order/list", "/api/order/updateStatus", "/api/product/**"))
                .excludePathPatterns(Arrays.asList("/api/product/fetchProduct/**", "/api/product/fetchProductList", "/api/product/search"));
    }
}
