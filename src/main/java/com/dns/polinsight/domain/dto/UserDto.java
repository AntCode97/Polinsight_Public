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

  private Long id;

  private Long point;

  private String email;

  private String phone;

  private String recommend;

  private String name;

  private UserRoleType role;

  private Boolean isEmailReceive;

  private Boolean isSmsReceive;


  public UserDto(User user) {
    this.id = user.getId();
    this.point = user.getPoint();
    this.email = user.getEmail();
    this.name = user.getName();
    this.phone = user.getPhone();
    this.role = user.getRole();
    this.isEmailReceive = user.getIsEmailReceive();
    this.isSmsReceive = user.getIsSmsReceive();
    this.recommend = user.getRecommend();
  }

}
