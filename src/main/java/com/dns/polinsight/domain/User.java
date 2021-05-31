package com.dns.polinsight.domain;

import lombok.*;
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
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails, Serializable {


  private static final long serialVersionUID = 7723866521224716971L;


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String email;


  private String password;


  private String name;

  @Column(name = "role")
  @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole.class)
  private List<UserRole> rolse = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return rolse.stream().map(auth -> new SimpleGrantedAuthority(auth.toString())).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
