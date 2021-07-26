package com.dns.polinsight.domain;

import com.dns.polinsight.types.ProgressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SurveyStatus {


  // 서베이몽키에서 등록한 설문의 커스텀 변수
  @ElementCollection
  @Builder.Default
  @Column(name = "variables")
  @Setter
  private Set<String> variables = new HashSet<>();

  // 설문을 완료한 사람 수
  @Builder.Default
  @Setter
  private Integer count = 0;

  @Enumerated(EnumType.STRING)
  private ProgressType progressType = ProgressType.BEFORE;

  // 포인트가 지급 될 수 있는 최소 시간
  private LocalDateTime minimumTime;


  public void setProgressType(LocalDateTime endDateTime) {
    try {
      int cmp = LocalDateTime.now().compareTo(endDateTime);
      if (cmp < 0) {
        this.progressType = ProgressType.ONGOING;
      } else {
        this.progressType = ProgressType.END;
      }
    } catch (Exception e) {
      this.progressType = ProgressType.BEFORE;
    }
  }

}
