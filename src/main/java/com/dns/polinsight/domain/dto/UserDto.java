package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  @ElementCollection
  private final List<String> favorite = new ArrayList<>();

  private Long id;

  private Long point;

  private Email email;

  private Phone phone;

  private Phone recommend;

  private Address address;

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
    this.email = user.getEmail();
    this.name = user.getName();
    this.phone = user.getPhone();
    this.role = user.getRole();
    this.isEmailReceive = user.getIsEmailReceive();
    this.isSmsReceive = user.getIsSmsReceive();
    this.recommend = user.getRecommend();
    this.gender = user.getPanel().getGender();
    this.education = user.getPanel().getEducation();
    this.marry = user.getPanel().getMarry();
    this.birth = user.getPanel().getBirth();
    this.birthType = user.getPanel().getBirthType();
    this.job = user.getPanel().getJob();
    this.industry = user.getPanel().getIndustry();
    this.favorite.addAll(user.getPanel().getFavorite());
    this.registeredAt = user.getRegisteredAt();
  }

}
