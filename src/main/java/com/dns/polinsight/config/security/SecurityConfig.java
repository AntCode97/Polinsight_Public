package com.dns.polinsight.config.security;

import com.dns.polinsight.config.oauth.CustomOAuth2Service;
import com.dns.polinsight.domain.UserRole;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService service;

  private final AuthenticationSuccessHandler successHandler;

  private final LogoutSuccessHandler logoutSuccessHandler;

  private final CustomOAuth2Service customOAuth2Service;

  private final AuthenticationFailureHandler failureHandler;

  /* 인증 실패 처리 */
  private final AuthenticationEntryPoint entryPoint;

  /* 인가 실패 처리 */
  private final AccessDeniedHandler deniedHandler;

  @Value("${custom.permission.resources}")
  private String[] staticResources;

  @Value("${custom.permission.admin}")
  private String[] permitAdmin;

  @Value("${custom.permission.template}")
  private String[] templates;

  @Value("${custom.permission.swagger-ui}")
  private String[] swagger;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(service).passwordEncoder(passwordEncoder());
  }

  //  @Override
  //  public void configure(WebSecurity web) throws Exception {
  //    //    static 자원들은 신경쓰지 않음 --> security filter chain을 거치지 않음
  //    // but 문제 발생 소지 많음
  //    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  //  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
          .csrf().disable()
          .cors().disable()
          .authorizeRequests()
          .antMatchers(staticResources).permitAll()
          .antMatchers(permitAdmin).hasAuthority(UserRole.ADMIN.name())  // Swagger 접근 허가
          .antMatchers(templates ).permitAll()
          .antMatchers(swagger ).permitAll()
          .anyRequest().authenticated()
        .and()
          .exceptionHandling()
          .authenticationEntryPoint(entryPoint)
          .accessDeniedHandler(deniedHandler)
        .and()
          .formLogin()
            .loginPage("/loginpage")
            .loginProcessingUrl("/dologin")
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(successHandler)
            .failureHandler(failureHandler)
        .and()
            .logout()
              .logoutUrl("/dologout")
              .logoutSuccessHandler(logoutSuccessHandler)
              .deleteCookies("JSESSIONID")
              .clearAuthentication(true)
              .invalidateHttpSession(true)
        .and()
          .httpBasic().disable()
          .oauth2Login()
            .loginPage("/loginpage")
            .successHandler(successHandler)
            .userInfoEndpoint()
            .userService(customOAuth2Service)
    ;
    // @formatter:on
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CustomAuthProccessingFilter customAuthProccessingFilter() {
    CustomAuthProccessingFilter filter = new CustomAuthProccessingFilter("/dologin");
    filter.setAuthenticationManager(authManager());
    return filter;
  }

  @Bean
  public CustomAuthManager authManager() {
    return new CustomAuthManager(service);
  }

}
