package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.types.Address;
import com.dns.polinsight.types.GenderType;
import com.dns.polinsight.types.convereter.AddressAttrConverter;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Panel implements Serializable {


  @Setter
  @Fetch(FetchMode.SELECT)
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> favorite = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private GenderType gender;

  private String education;

  private String marry;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birth;

  private String birthType;

  private String job;

  private String industry;

  @Convert(converter = AddressAttrConverter.class, attributeName = "address")
  private Address address;

  public static Panel of(UserDto dto) {
    return Panel.builder()
                .address(Address.of(dto.getAddress()))
                .job(dto.getJob())
                .industry(dto.getIndustry())
                .birth(dto.getBirth())
                .birthType(dto.getBirthType())
                .marry(dto.getMarry())
                .build();
  }

  public void update(Panel panel) {
    this.gender = panel.getGender();
    this.education = panel.getEducation();
    this.marry = panel.getMarry();
    this.birth = panel.getBirth();
    this.birthType = panel.getBirthType();
    this.job = panel.getJob();
    this.industry = panel.getIndustry();
    this.favorite.addAll(panel.getFavorite());
  }

}
