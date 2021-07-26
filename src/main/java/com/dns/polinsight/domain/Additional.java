package com.dns.polinsight.domain;

import com.dns.polinsight.types.GenderType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.Date;
import java.util.List;

@Embeddable
@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Additional {

  /*
   * 추가정보 클래스
   * */
  private GenderType gender;

  private String education;

  private Boolean marry;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birth;

  private String birthType;

  private String job;

  private String industry;

  @ElementCollection
  private List<String> favorite;

}
