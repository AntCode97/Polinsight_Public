package com.dns.polinsight.domain;

import com.dns.polinsight.types.GenderType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
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
  private final List<String> favorite= new ArrayList<>();

}
