package com.belong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket swaggerNumberApi1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("numbers-api-v1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.belong.controller"))
                .paths(PathSelectors.regex("/numbers/v1.*"))
                .build().apiInfo(new ApiInfoBuilder().version("1.0").title("Numbers API").description("Documentation Numbers API v1").build());
    }

    @Bean
    public Docket swaggerNumberApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("numbers-api-v2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.belong.controller"))
                .paths(PathSelectors.regex("/numbers/v2.*"))
                .build().apiInfo(new ApiInfoBuilder().version("2.0").title("Numbers API").description("Documentation Numbers API v2").build());
    }
}
