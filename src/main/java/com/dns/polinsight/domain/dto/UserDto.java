package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.GenderType;
import com.dns.polinsight.types.UserRoleType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto implements Serializable {

  private static final long serialVersionUID = -2905913740026912191L;

  private List<String> favorite;

  @Setter
  private Long id;

  private Long point;

  private String email;

  private String password;

  private String newPassword;

  private String phone;

  private String recommend;

  private String address;

  private String name;

  private UserRoleType role;

  private LocalDate registeredAt;

  private Boolean isEmailReceive;

  private Boolean isSmsReceive;

  @Enumerated(EnumType.STRING)
  private GenderType gender;

  private String education;

  private String marry;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birth;

  private String birthType;

  private String job;

  private String industry;

  public UserDto(User user) {
    this.id = user.getId();
    this.point = user.getPoint();
    this.email = user.getEmail().toString();
    this.name = user.getName();
    this.phone = user.getPhone().toString();
    this.role = user.getRole();
    this.isEmailReceive = user.getIsEmailReceive();
    this.isSmsReceive = user.getIsSmsReceive();
    this.recommend = user.getRecommend() == null ? "" : user.getRecommend().toString();
    this.registeredAt = user.getRegisteredAt();
    if (user.getPanel() != null) {
      this.gender = user.getPanel().getGender();
      this.education = user.getPanel().getEducation();
      this.marry = user.getPanel().getMarry();
      this.birth = user.getPanel().getBirth();
      this.birthType = user.getPanel().getBirthType();
      this.job = user.getPanel().getJob();
      this.industry = user.getPanel().getIndustry();
      this.favorite = user.getPanel().getFavorite();
      this.address = user.getPanel().getAddress() == null ? "" : user.getPanel().getAddress().toString();
    }
  }

}
