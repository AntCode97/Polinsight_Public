package com.dns.polinsight.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

  @Id
  private Long id;

  private Long surveyId;

  private String title;

  private String nickname;

  private String href;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  private LocalDateTime endAt;

  private String preview;

  private Long point;


  @OneToOne
  @JoinColumn(name = "status_id")
  private SurveyStatus status;

}
