package com.dns.polinsight.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
@ToString
public class User implements UserDetails, Serializable {

  private static final long serialVersionUID = 7723866521224716971L;

  @OneToMany(mappedBy = "user")
  private final List<Board> boards = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Email
  private String email;

  @Size(min = 10, max = 16)
  private String password;

  private String name;

  private String picture;


  @Size(min = 11, max = 11)
  private String phone;

  @Size(min = 11, max = 11)
  private String recommend;


  @Min(0)
  private Long point;

  @NotNull
  @Column(name = "role")
  private UserRole role;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role.name()));
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


  public User update(String name, String picture) {
    this.name = name;
    this.picture = picture;
    return this;
  }

  public User pointUpdate(Long point) {
    this.point = point;
    return this;
  }

}
