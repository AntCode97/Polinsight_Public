package com.dns.polinsight.domain;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class SignupDTO {

  @Email
  private String email;

  private String domain;

  @NotNull
  private String password;

  @NotNull
  private String name;

  @NotNull
  private String phone;

  private String recommend;

  private boolean ispanel;


  public User toUser() {
    return User.builder()
               .email(this.email + "@" + this.domain)
               .name(this.name)
               .password(this.password)
               .phone(this.phone)
               .recommend(this.recommend)
               .build();
  }

}
