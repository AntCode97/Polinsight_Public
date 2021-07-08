package com.dns.polinsight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private final List<Board> boards = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @PositiveOrZero
  private Long id;

  @NotNull
  @UniqueElements
  @Email(message = "이메일의 형식이 다릅니다.")
  private String email;

  @Size(min = 10, max = 16, message = "패스워드 길이가 맞지 않습니다.")
  private String password;

  private String name;

  private String picture;

  //  @Enumerated(EnumType.STRING) ???????R
  private Gender gender;

  //  @Temporal(TemporalType.DATE)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birth;

  private String birthType;

  @Size(min = 11, max = 11)
  private String phone;

  @Size(min = 11, max = 11)
  private String recommend;

  private String education;

  private boolean marry;

  private String job;

  private String industry;

  @ElementCollection
  private List<String> favorite;

  @PositiveOrZero
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

  public User addMoreInfo() {
    return this;
  }

}
