package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Panel;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;

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
  private String phone;

  private String recommend;

  private boolean ispanel;

  @Builder.Default
  private Panel panel = new Panel();

}
