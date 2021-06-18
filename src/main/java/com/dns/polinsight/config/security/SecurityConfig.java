package com.dns.polinsight.config.security;

import com.dns.polinsight.config.oauth.CustomOAuth2Service;
import com.dns.polinsight.domain.UserRole;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
  public void configure(WebSecurity web) throws Exception {
//    static 자원들은 신경쓰지 않음 --> security filter chain을 거치지 않음
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
          .csrf().disable()
          .cors().disable()
          .authorizeRequests()
          .antMatchers("/swagger-ui/**").hasAuthority(UserRole.ADMIN.name())  // Swagger 접근 허가
          .antMatchers("/signup", "/index","/", "/404","loginSuccess", "/loginpage" ).permitAll()
          .anyRequest().authenticated()
        .and()
          .formLogin()
            .loginPage("/loginpage")
            .loginProcessingUrl("/dologin")
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(successHandler)
            .failureForwardUrl("/signup")
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
