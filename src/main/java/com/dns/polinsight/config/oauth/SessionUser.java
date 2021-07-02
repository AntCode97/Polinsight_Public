package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Builder
@RequiredArgsConstructor
public class SessionUser implements Serializable {

  @NotNull
  private final String name;

  private final String id;

  @Email
  private final String email;

  private final String picture;


  private final UserRole role;

  private final Long point;

  public SessionUser(User user) {
    this.name = user.getName();
    this.id = user.getId();
    this.point = user.getPoint();
    this.role = user.getRole();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }

}
