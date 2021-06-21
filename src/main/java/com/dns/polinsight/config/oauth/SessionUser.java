package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.SocialType;
import com.dns.polinsight.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

  @NotNull
  private String name;

  //  @Email // TODO: 2021/06/09  주석 해제 필요
  private String email;

  private String picture;

  private SocialType type;

  public SessionUser(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
    this.type = user.getSocial();
  }

}