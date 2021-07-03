package com.dns.polinsight.config.security;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {

  private final UserService service;

  private final PasswordEncoder passwordEncoder;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();

    User user;
    Collection<? extends GrantedAuthority> authorities;

    try {
      user = (User) service.loadUserByUsername(name);
      System.out.println("-------------------------------------------------\n" + user.toString() + "\n-------------------------------------------------");
      if (!passwordEncoder.matches(password, user.getPassword())) {
        log.info("비밀번호 일치하지 않음------------------------------------------------------");
        log.info("------------------------------------------------------");
        //        throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
      }
      authorities = user.getAuthorities();
    } catch (UsernameNotFoundException e) {
      log.info(e.toString());
      throw new UsernameNotFoundException(e.getMessage());
    } catch (BadCredentialsException e) {
      log.info(e.toString());
      throw new BadCredentialsException(e.getMessage());
    } catch (Exception e) {
      log.info(e.toString());
      throw new RuntimeException(e.getMessage());
    }

    return new UsernamePasswordAuthenticationToken(user, password, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}
