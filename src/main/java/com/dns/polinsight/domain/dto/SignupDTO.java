package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
  private String password;

  @NotEmpty
  private String name;

  @NotEmpty
  @Size(min = 11, max = 11)
  private String phone;

  @Size(min = 11, max = 11)
  private String recommend;

  private boolean ispanel;


  public User toUser(PasswordEncoder passwordEncoder) {
    return User.builder()
               .email(this.email)
               .password(passwordEncoder.encode(this.password))
               .name(this.name)
               .phone(this.phone)
               .recommend(this.recommend)
               .build();
  }

}
