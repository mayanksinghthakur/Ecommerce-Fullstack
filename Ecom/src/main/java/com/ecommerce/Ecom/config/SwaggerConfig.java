package com.ecommerce.Ecom.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI CustomOpenAPI(){

        //this defines securoty scheme for bearer token
        //and tells swagger ui that we want to use jwt and pass jwt as the header
        SecurityScheme beareScheme=new SecurityScheme().type
                //tells that it is http based scheme
                (SecurityScheme.Type.HTTP).
                //this tells swagger to use bearer type scheme
                scheme("bearer").
                bearerFormat("JWT").
                //description
                description("JWT Bearer Token");

//here we will tell that above security scheme is required for all the http request
        SecurityRequirement bearerRequirement =new SecurityRequirement().addList("Bearer Authentication");


        //now define the Open api object that will be returned
        return new OpenAPI().
                //for reflecting information at the top of the api
        info(new Info().title("Spring Boot eCommerce API").version("1.0").description("This is a springboot project for the ecommerce").license(new License().name("Apache 2.0").url("http://ecommerce.com")).contact(new Contact().name("mayank").email("mayank1208@gmail.com").url("https://github.com/ "))).externalDocs(new ExternalDocumentation().description("Project Documentation ").url("http://ecom.com")).
                //adding all the components
                components(new Components()
                //key name is from SecurityRequirement().addList("Bearer Authentication");
                .addSecuritySchemes("Bearer Authentication",beareScheme)).addSecurityItem(bearerRequirement);

    }
}
