package com.toqqa.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties("spring.mail")
public class EmailProps {

	private String username;
	private String password;
	private Integer port;
	private String protocol;
	
	

}
