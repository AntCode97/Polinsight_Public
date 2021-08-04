package com.dns.polinsight.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParticipateSurveyDto {

  private Long userId;

  private String email;

  private Long surveyId;

  private String title;

  private LocalDateTime participatedAt;

  private Long point;

  private String hash;

  @Setter
  private Boolean finished;

}
