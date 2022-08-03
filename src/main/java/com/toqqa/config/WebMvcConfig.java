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
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public WebMvcConfig(Interceptor interceptor,
                        CustomerInterceptor customerInterceptor,
                        AdminInterceptor adminInterceptor) {
        this.interceptor = interceptor;
        this.customerInterceptor = customerInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE").maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**", "/api/order/list", "/api/order/updateStatus")
                .excludePathPatterns(Arrays.asList("/api/auth/**", "/api/otp", "/api/registration/**", "/api/admin/**",
                        "/swagger-ui/**", "/configuration/**", "/swagger-resources/**",
                        "/v2/api-docs", "/webjars/**", "/api/business/**", "/file/upload/**", "/api/business/**",
                        "/api/customer/**", "api/cart/**", "/api/address/**", "/api/device/**", "/api/favourites/**",
                        "/api/feedback/**", "/api/notification/**", "/api/order/**", "/api/product/categories/sme", "/api/product/categories",
                        "/api/product/fetchProduct/**", "/api/product/fetchProductList", "/api/product/search", "/api/product/productsubcategories", "/api/sme/productList",
                        "/api/user/**", "/api/wishlist/**", "/api/sme/fetchSme/**", "/api/rating/**"));
        registry.addInterceptor(customerInterceptor).addPathPatterns(Arrays.asList("/api/advertisement/**", "/api/agent/**", "/api/order/list",
                        "/api/order/updateStatus", "/api/product/**", "/api/sme/**"))
                .excludePathPatterns(Arrays.asList("/api/product/fetchProduct/**", "/api/product/fetchProductList",
                        "/api/product/search", "/api/advertisement/updateClicks/**", "/api/product/categories/sme", "/api/product/categories",
                        "/api/product/productsubcategories", "/api/sme/productList", "/api/sme/fetchSme/**"));
        registry.addInterceptor(adminInterceptor).addPathPatterns("/api/registration/superuser");
    }
}
