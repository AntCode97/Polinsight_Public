package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  /*
   * Server -> Client에서 유저 정보를 전송할 때 사용
   * */

  private Long id;

  private Long point;

  private String email;

  private String phone;

  private UserRoleType role;

  private Boolean isEmailReceive;

  private Boolean isSMSReceive;

  public UserDto(User user) {
    this.id = user.getId();
    this.point = user.getPoint();
    this.email = user.getEmail();
    this.phone = user.getPhone();
    this.role = user.getRole();
    this.isEmailReceive = user.getIsEmailReceive();
    this.isSMSReceive = user.getIsSMSReceive();
  }

}
