package com.dns.polinsight.config.security;

import com.dns.polinsight.config.oauth.CustomOAuth2Service;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService service;

  private final AuthenticationSuccessHandler successHandler;

  private final LogoutSuccessHandler logoutSuccessHandler;

  private final CustomOAuth2Service customOAuth2Service;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(service).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
          .csrf().disable()
          .cors().disable()
          .authorizeRequests()
          .antMatchers("/static/**").permitAll()  // 정적 리소스 접근 허가
          .antMatchers("/login","/signup", "/index","/", "/404","loginSuccess", "/loginpage" ).permitAll()
          .anyRequest().authenticated()
        .and()
          .formLogin()
            .loginPage("/loginpage")
            .loginProcessingUrl("/dologin")
            .usernameParameter("email")
            .passwordParameter("pw")
            .successHandler(successHandler)
            .failureForwardUrl("/signup")
        .and()
            .logout()
              .logoutUrl("/dologout")
              .logoutSuccessHandler(logoutSuccessHandler)
        .and()
            .oauth2Login()
              .loginPage("/loginpage")
              .successHandler(successHandler)
              .userInfoEndpoint()
                .userService(customOAuth2Service)
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
