package com.dns.polinsight.projection;

import com.dns.polinsight.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class ExcelUserMapping {

  private Long point;

  private String email;

  private String phone;

  private String recommend;

  private String address;

  private String name;

  private LocalDate registeredAt;

  private Boolean isEmailReceive;

  private Boolean isSmsReceive;

  private LocalDate birth;

  private String birthType;

  private String job;

  private String industry;

  public static ExcelUserMapping of(User user) {
    return ExcelUserMapping.builder()
                           .point(user.getPoint())
                           .email(String.valueOf(user.getEmail()))
                           .phone(String.valueOf(user.getPhone()))
                           .recommend(String.valueOf(user.getRecommend()))
                           .address(String.valueOf(user.getPanel().getAddress()))
                           .name(user.getName())
                           .registeredAt(user.getRegisteredAt())
                           .isEmailReceive(user.getIsEmailReceive())
                           .isSmsReceive(user.getIsSmsReceive())
                           .birth(user.getPanel().getBirth())
                           .birthType(user.getPanel().getBirthType())
                           .job(user.getPanel().getJob())
                           .industry(user.getPanel().getIndustry())
                           .build();
  }

}
