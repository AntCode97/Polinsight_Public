package com.dns.polinsight.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Survey implements Serializable {

  private static final long serialVersionUID = -4701183897615758658L;

  @Setter
  @Embedded
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  private SurveyStatus status;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long surveyId;

  private String href;

  private String title;

  //  private LocalDateTime modifiedAt;

  @Setter
  private LocalDateTime createdAt;

  private LocalDateTime endAt;

  private Long point;

}
