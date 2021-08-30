package com.dns.polinsight.domain.dto;

import com.dns.polinsight.types.GenderType;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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

  private List<String> favorite;

  private GenderType gender;

  private String education;

  private String marry;

  private String birth;

  private String birthType;

  private String job;

  private String industry;

  private String address;

}
