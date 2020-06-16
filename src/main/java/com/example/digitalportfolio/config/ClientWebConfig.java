package com.example.digitalportfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ClientWebConfig implements WebMvcConfigurer
{
  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor()
  {
    return new MethodValidationPostProcessor();
  }

}
