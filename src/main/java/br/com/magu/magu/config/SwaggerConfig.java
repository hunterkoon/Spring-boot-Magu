package br.com.magu.magu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi(){
       return new Docket (DocumentationType.SWAGGER_2)
               .select()
               .apis(RequestHandlerSelectors.basePackage("br.com.magu.magu"))
               .paths(regex("/api/v1/magu.*"))
               .build()
               .apiInfo(metainfo());


    }

    private ApiInfo metainfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Magu",
                "API BFF",
                "1.0",
                "Terms of Servide",
                new Contact("Lojas Magu", "https://lojasmagu.com.br", "lojasmagu@store.com.br"),
                "Apache License Version 2.0",
                "https://apache.org/license.html",
                new ArrayList<VendorExtension>()
        );
        return apiInfo;
    }

}