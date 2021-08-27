package com.dns.polinsight.config.security;

import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthManager implements AuthenticationManager {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails details = userService.loadUserByUsername(String.valueOf(authentication.getPrincipal()));
    String inputPassword = authentication.getCredentials().toString();
    log.warn(inputPassword);
    log.warn(details.getPassword());
    
    if (!passwordEncoder.matches(inputPassword, details.getPassword()))
      throw new BadCredentialsException("비밀번호가 일치하지 않습니다");

    return new UsernamePasswordAuthenticationToken(
        details.getUsername(),
        details.getPassword(),
        details.getAuthorities());
  }

}
