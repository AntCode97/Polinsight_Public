package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class SessionUser implements Serializable {

  @NotNull
  private String name;

  @NotNull
  private Long id;

  @Email
  private String email;

  private String picture;

  private UserRole role;

  private Long point;

  public SessionUser(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.name = user.getName();
    this.point = user.getPoint();
    this.picture = user.getPicture();
  }

  public SessionUser of(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.name = user.getName();
    this.point = user.getPoint();
    this.picture = user.getPicture();
    return this;
  }

}
