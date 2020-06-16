package com.example.digitalportfolio.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/user/confirm").permitAll()
        .antMatchers(HttpMethod.GET, "/api/v1/user/confirm").permitAll()
        .antMatchers("/api/**").authenticated()
        .and()
        .httpBasic();

    http.csrf().disable();
    http.headers().frameOptions().disable();


  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }

}






