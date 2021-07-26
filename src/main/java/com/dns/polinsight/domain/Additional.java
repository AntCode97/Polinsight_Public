package com.dns.polinsight.domain;

import com.dns.polinsight.types.GenderType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Embeddable
@ToString
//@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Additional  {

//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "id", nullable = false)
//  private Long id;

  @OneToOne(mappedBy = "additional")
  private User user;

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

  public Additional update(User user) {
    this.user = user;
    return this;
  }

}
