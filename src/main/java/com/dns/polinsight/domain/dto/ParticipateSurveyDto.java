package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.ParticipateSurvey;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ParticipateSurveyDto {

  private String email;

  private String title;

  private String participatedAt;

  private Long point;

  private Boolean finished;

  //  public ParticipateSurveyDto(ParticipateSurvey participateSurvey) {
  //    this.title = participateSurvey.getSurvey().getTitle();
  //    this.email = participateSurvey.getUser().getEmail().toString();
  //    this.point = participateSurvey.getSurveyPoint();
  //    this.participatedAt = String.valueOf(participateSurvey.getParticipatedAt());
  //    this.finished = participateSurvey.getFinished();
  //  }

  public static ParticipateSurveyDto of(ParticipateSurvey ps, String title) {
    return ParticipateSurveyDto.builder()
                               .title(title)
                               .email(ps.getUser().getEmail().toString())
                               .point(ps.getSurveyPoint())
                               .participatedAt(ps.getParticipatedAt().toString())
                               .finished(ps.getFinished())
                               .build();
  }

}
