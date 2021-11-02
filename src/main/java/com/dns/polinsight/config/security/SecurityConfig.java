package com.dns.polinsight.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final LogoutSuccessHandler logoutSuccessHandler;

  private final AuthenticationFailureHandler failureHandler;

  /* 인증 실패 처리 */
  private final AuthenticationEntryPoint entryPoint;

  private final CustomSuccessHandler successHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors().disable()
        .authorizeRequests()
        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/dologin")
        .usernameParameter("email")
        .passwordParameter("password")
        .successHandler(successHandler)
        .failureHandler(failureHandler)
        .permitAll()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(entryPoint)
        .accessDeniedPage("/denied")
        .and()
        .logout()
        .logoutUrl("/dologout")
        .logoutSuccessHandler(logoutSuccessHandler)
        .logoutSuccessUrl("/")
        .deleteCookies("JSESSIONID")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .and()
        .httpBasic().disable();

    http.sessionManagement()
        .sessionAuthenticationErrorUrl("/")
        .invalidSessionUrl("/")
        .maximumSessions(1)
        .maxSessionsPreventsLogin(false)
        .expiredUrl("/");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

}
