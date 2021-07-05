package com.dns.polinsight.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class SignupDTO {

  private String email;

  private String domain;

  private String password;

  private String name;

  private String phone;

  private String recommend;

  private boolean ispanel;


  public User toUser(){
    return User.builder()
               .email(this.email+"@"+this.domain)
               .name(this.name)
               .password(this.password)
               .phone(this.phone)
               .recommend(this.recommend)
               .build();
  }
}
