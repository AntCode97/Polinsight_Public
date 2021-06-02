package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

  @NotNull
  private String name;

  @Email
  private String email;

  private String picture;

  public SessionUser(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }

}
