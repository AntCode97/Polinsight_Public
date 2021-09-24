package com.dns.polinsight.config.security;

import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsUtils;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;

  private final LogoutSuccessHandler logoutSuccessHandler;

  private final AuthenticationFailureHandler failureHandler;

  /* 인증 실패 처리 */
  private final AuthenticationEntryPoint entryPoint;

  /* 인가 실패 처리 */
  private final AccessDeniedHandler deniedHandler;

  private final PathPermission permission;

  private final CustomSuccessHandler successHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors().and()
        .authorizeRequests()
        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        //        .antMatchers(permission.getAnonymous()).permitAll()
        //        .antMatchers(permission.getResources()).permitAll()
        //        .antMatchers(permission.getUser()).hasAuthority(UserRoleType.USER.name())
        //        .antMatchers(permission.getPanel()).hasAuthority(UserRoleType.PANEL.name())
        //        .antMatchers(permission.getManager()).hasAuthority(UserRoleType.MANAGER.name())
        //        .antMatchers(permission.getAdmin()).hasAuthority(UserRoleType.ADMIN.name())
        //        .anyRequest().authenticated()
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
        .logoutSuccessUrl("/")
        .deleteCookies("JSESSIONID")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .and()
        .httpBasic().disable();
    //        .addFilterBefore(customAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

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

  //  @Bean
  public CustomAuthenticationFilter customAuthenticationProcessingFilter() {
    CustomAuthenticationFilter filter = new CustomAuthenticationFilter("/dologin");
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationFailureHandler(failureHandler);
    filter.setAuthenticationSuccessHandler(successHandler);
    return filter;
  }

  //  @Bean
  public AuthenticationManager authenticationManager() {
    return new CustomAuthManager(userService, passwordEncoder());
  }

}
