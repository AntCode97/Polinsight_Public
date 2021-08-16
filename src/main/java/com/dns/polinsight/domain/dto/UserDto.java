package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.GenderType;
import com.dns.polinsight.types.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  private LocalDate registeredAt;

  private Boolean isEmailReceive;

  private Boolean isSmsReceive;

  @ElementCollection
  private final List<String> favorite = new ArrayList<>();

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
    this.email = user.getEmail();
    this.name = user.getName();
    this.phone = user.getPhone();
    this.role = user.getRole();
    this.isEmailReceive = user.getIsEmailReceive();
    this.isSmsReceive = user.getIsSmsReceive();
    this.recommend = user.getRecommend();
    this.gender = user.getAdditional().getGender();
    this.education = user.getAdditional().getEducation();
    this.marry = user.getAdditional().getMarry();
    this.birth = user.getAdditional().getBirth();
    this.birthType = user.getAdditional().getBirthType();
    this.job = user.getAdditional().getJob();
    this.industry = user.getAdditional().getIndustry();
    this.favorite.addAll(user.getAdditional().getFavorite());
    this.registeredAt = user.getRegisteredAt();
  }

}
