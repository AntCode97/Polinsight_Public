package com.dns.polinsight.config.security;

import com.dns.polinsight.config.oauth.CustomOAuth2Service;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;

  private final LogoutSuccessHandler logoutSuccessHandler;

  private final CustomOAuth2Service customOAuth2Service;

  private final AuthenticationFailureHandler failureHandler;

  /* 인증 실패 처리 */
  private final AuthenticationEntryPoint entryPoint;

  /* 인가 실패 처리 */
  private final AccessDeniedHandler deniedHandler;

  private final PathPermission permission;

  @Autowired
  @Qualifier("customSuccessHandler")
  private CustomSuccessHandler successHandler;

  @Autowired
  @Qualifier("rememberMeSuccessHandler")
  private RemeberMeSuccessHandler remeberMeSuccessHandler;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors().disable()
        .authorizeRequests()
        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        .antMatchers(permission.getTemplate().toArray(new String[0])).permitAll()
        .antMatchers(permission.getResources().toArray(new String[permission.getResources().size()])).permitAll()
        .antMatchers(permission.getAdmin().toArray(new String[permission.getAdmin().size()])).hasAuthority(UserRoleType.ADMIN.name())  // Swagger 접근 허가
        .anyRequest().authenticated().and()
        .rememberMe()
        .key("remeberMeSecretKey")
        .rememberMeParameter("rememberMe")
        .tokenValiditySeconds(7 * 24 * 60 * 60)  // 7일
        .useSecureCookie(true)
        .userDetailsService(userService)
        .authenticationSuccessHandler(remeberMeSuccessHandler)
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
        .accessDeniedHandler(deniedHandler).accessDeniedPage("/denied")
        .and()
        .logout()
        .logoutUrl("/dologout")
        .logoutSuccessHandler(logoutSuccessHandler)
        .deleteCookies("JSESSIONID")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .and()
        .sessionManagement()
        .sessionAuthenticationErrorUrl("/login").invalidSessionUrl("/login")
        .sessionConcurrency(concurrencyControlConfigurer -> {
          concurrencyControlConfigurer.expiredUrl("/login");
          concurrencyControlConfigurer.maximumSessions(1);  // 최대 한개의 로그인만 허용
          concurrencyControlConfigurer.maxSessionsPreventsLogin(true);
        })
        .and()
        .httpBasic().disable()
        .oauth2Login()
        .loginPage("/login")
        .successHandler(successHandler)
        .userInfoEndpoint()
        .userService(customOAuth2Service);
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
