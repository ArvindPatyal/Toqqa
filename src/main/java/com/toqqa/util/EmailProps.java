package com.toqqa.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("spring.mail")
public class EmailProps {

    private String username;
    private String password;
    private Integer port;
    private String protocol;
    private String adminEmail;


}
