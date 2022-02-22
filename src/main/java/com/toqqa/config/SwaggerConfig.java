package com.toqqa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(metaData()).select()
//				.apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.toqqa.controller")).paths(PathSelectors.any()).build();
	}

	private ApiInfo metaData() {

		Contact contact = new Contact("Biz Tecno", "https://biztecno.net/", "rajpal@biztecno.net");
		return new ApiInfoBuilder().title("TOQQA REST API Document").description("TOQQA Digital Ecosystem")
				.version("1.0.0").license("Apache 2.0").contact(contact)
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0").build();
	}
}