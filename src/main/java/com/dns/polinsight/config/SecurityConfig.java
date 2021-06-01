package com.dns.polinsight.config;

import com.dns.polinsight.service.UserServlce;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserServlce service;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(service).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
          .authorizeRequests()
          .antMatchers("/static/**").permitAll()
          .antMatchers("/login","/signup", "/index","/", "/404", "/signup").permitAll()
          .anyRequest().authenticated()
        .and()
          .formLogin()
          .loginPage("/login")
          .usernameParameter("email")
          .passwordParameter("password")
          .successForwardUrl("/index")
          .failureForwardUrl("/signup")
        .and()
          .csrf().disable()

//          .oauth2Login()
//        .and()

    ;
    // @formatter:on
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
