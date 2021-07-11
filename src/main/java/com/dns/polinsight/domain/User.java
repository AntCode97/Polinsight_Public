package com.dns.polinsight.domain;

import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.types.UserRoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
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
@ToString(exclude = "boards")
public class User implements UserDetails, Serializable {

  private static final long serialVersionUID = 7723866521224716971L;

  /*
   * 유저 기본정보 클래스
   * */
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Board> boards = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @PositiveOrZero
  private Long id;

  private String email;

  private String password;

  private String name;

  @Size(min = 11, max = 11)
  private String phone;

  @Size(min = 11, max = 11)
  private String recommend;

  @PositiveOrZero
  private Long point;

  private UserRoleType role;

  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "additional_id")
  private Additional additional;

  public void setBoards(List<Board> boards) {
    this.boards = boards;
  }

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

  public User update(SessionUser sessionUser) {
    this.name = sessionUser.getName();
    this.email = sessionUser.getEmail();
    this.role = sessionUser.getRole();
    this.point = sessionUser.getPoint() == null ? 0 : sessionUser.getPoint();
    return this;
  }

  public User update(Additional additional) {
    this.additional = additional;
    return this;
  }

}
