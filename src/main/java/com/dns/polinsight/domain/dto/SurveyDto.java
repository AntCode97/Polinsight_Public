package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.DenyAll;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyDto implements Comparable<SurveyDto> {

  private String title;

  private String participateUrl;

  private Long point;

  private Long surveyId;

  private Integer minimumTime;

  private LocalDate createdAt;

  private LocalDate endAt;

  @Enumerated(EnumType.STRING)
  private CollectorStatusType status;

  private Long count;

  @Enumerated(EnumType.STRING)
  private ProgressType progress;


  @Override
  public int compareTo(@NotNull SurveyDto o) {
    if (this.getProgress().compareTo(o.getProgress()) < 0) {
      return -1;
    } else if (this.getProgress().compareTo(o.getProgress()) == 0) {
      return this.getEndAt().compareTo(o.getEndAt());
    }
    return 1;
  }

}
