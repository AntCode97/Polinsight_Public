//package com.dns.polinsight.service;
//
//import com.dns.polinsight.config.security.CustomAuthManager;
//import com.dns.polinsight.types.UserRoleType;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Slf4j
////@Service
////@RequiredArgsConstructor
//public class LoginService {
//
//  //  private final CustomAuthManager authManager;
//
//  public void login(String email, String password) {
//    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//        email,
//        password,
//        List.of(new SimpleGrantedAuthority(UserRoleType.USER.name()))
//    );
//
//    Authentication auth = authManager.authenticate(token);
//    SecurityContextHolder.getContext().setAuthentication(auth);
//  }
//
//}
