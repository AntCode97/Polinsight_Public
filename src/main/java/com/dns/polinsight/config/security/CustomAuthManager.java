package com.dns.polinsight.config.security;

import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthManager implements AuthenticationManager {

  private final UserService service;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails user = service.loadUserByUsername(authentication.getName());
    log.info("AuthManager-------------------------");
    return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
  }

}
