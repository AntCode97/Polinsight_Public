package com.dns.polinsight.domain;

import com.dns.polinsight.types.UserRoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.*;

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

  @Builder.Default
  @ElementCollection
  private final Set<Long> participateSurvey = new HashSet<>();

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private UserRoleType role = UserRoleType.USER;

  /*
   * 유저 기본정보 클래스
   * */
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  @Builder.Default
  @ToString.Exclude
  private List<Post> posts = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @PositiveOrZero
  private Long id;

  private String email;

  private String password;

  private String name;

  private String phone;

  private String recommend;

  @PositiveOrZero
  @Builder.Default
  @Setter
  private Long point = 0L;

  @Embedded
  private Additional additional;

  /*이메일 수신 동의 여부*/
  private Boolean isEmailReceive;

  /*문자 수신 동의 여부*/
  private Boolean isSMSReceive;

  public void addParticipateSurvey(long surveyId) {
    this.participateSurvey.add(surveyId);
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
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
