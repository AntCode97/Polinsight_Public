package com.dns.polinsight.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class SignupDTO {

  @NotEmpty
  private String email;

  @NotEmpty
  private String domain;

  @NotEmpty
  private String password;

  @NotEmpty
  private String name;

  @NotEmpty
  @Size(min = 11, max = 11)
  private String phone;

  @Size(min = 11, max = 11)
  private String recommend;

  @NotEmpty
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
