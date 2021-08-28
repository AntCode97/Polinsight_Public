//package com.dns.polinsight.config.security;
//
//import com.dns.polinsight.domain.User;
//import com.dns.polinsight.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//public class CustomAuthProvider implements AuthenticationProvider {
//
//  @Autowired
//  private UserService userService;
//
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//
//  @Override
//  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    String username = authentication.getName();
//    String password = String.valueOf(authentication.getCredentials());
//
//    User user = (User) userService.loadUserByUsername(username);
//
//    if (!passwordEncoder.matches(password, user.getPassword())) {
//      throw new BadCredentialsException("Provider -- 비밀번호 불일치");
//    }
//
//    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail().toString(), null, user.getAuthorities());
//
//    return token;
//  }
//
//  @Override
//  public boolean supports(Class<?> authentication) {
//    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//  }
//
//}
