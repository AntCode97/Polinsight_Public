package com.dns.polinsight.domain;

import com.dns.polinsight.types.GenderType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Embeddable
@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Additional implements Serializable {

  private static final long serialVersionUID = 4323740522466099195L;

  @ElementCollection
  private final List<String> favorite = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private GenderType gender;

  private String education;

  private Boolean marry;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birth;

  private String birthType;

  private String job;

  private String industry;

  public void update(Additional additional) {
    this.gender = additional.getGender();
    this.education = additional.getEducation();
    this.marry = additional.getMarry();
    this.birth = additional.getBirth();
    this.birthType = additional.getBirthType();
    this.job = additional.getJob();
    this.industry = additional.getIndustry();
    this.favorite.addAll(additional.getFavorite());
  }

}
