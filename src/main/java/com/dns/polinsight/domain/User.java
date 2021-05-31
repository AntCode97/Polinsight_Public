package com.dns.polinsight.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails, Serializable {


  private static final long serialVersionUID = 7723866521224716971L;


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String email;


  private String password;


  private String name;


  @OneToMany
  @Enumerated(EnumType.STRING)
  private List<UserType> type = new ArrayList<>();

  private List<SimpleGrantedAuthority> rolse = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return rolse.stream().map(auth -> new SimpleGrantedAuthority(auth.toString())).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

}
