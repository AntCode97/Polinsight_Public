package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.types.UserRoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicUpdate
public class User implements UserDetails, Serializable {

  private static final long serialVersionUID = 7723866521224716971L;

  @Builder.Default
  @ElementCollection
  @Column(name = "participate_survey_id")
  private Set<Long> participateSurvey = new HashSet<>();

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
  @Column(name = "is_email_receive")
  private Boolean isEmailReceive;

  /*문자 수신 동의 여부*/
  @Column(name = "is_sms_receive")
  private Boolean isSmsReceive;

  //  @Column(name = "registered_at")
  //  private LocalDateTime registeredAt;

  public User(UserDto dto) {
    this.id = dto.getId();
    this.point = dto.getPoint();
    this.name = dto.getName();
    this.email = dto.getEmail();
    this.phone = dto.getPhone();
    this.isSmsReceive = dto.getIsSmsReceive();
    this.isEmailReceive = dto.getIsEmailReceive();
    this.recommend = dto.getRecommend();
    this.participateSurvey = new HashSet<>();
    //    this.registeredAt = dto.getRegisteredAt();
  }

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
