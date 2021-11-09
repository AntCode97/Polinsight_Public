package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.SurveyDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
public class Survey implements Serializable {

  private static final long serialVersionUID = -9103994299951345908L;

  @Setter
  @Embedded
  @OrderBy("progress ASC")
  @Builder.Default
  private SurveyStatus status = new SurveyStatus();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true)
  private Long id;

  @Column(name = "survey_id", unique = true)
  private Long surveyId;

  private String href;

  private String title;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate createdAt;

  @Setter
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endAt;

  private Long point;

  @Setter
  private Long questionCount;

  @Setter
  private String thumbnail;

  @Setter
  private String originalName;

  @Setter
  @JsonManagedReference
  @OneToOne(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Collector collector;

  public static Survey of(SurveyDto dto) {
    return Survey.builder()
                 .id(dto.getId())
                 .surveyId(dto.getSurveyId())
                 .title(dto.getTitle())
                 .point(dto.getPoint())
                 .status(SurveyStatus.builder()
                                     .progress(dto.getProgress())
                                     .build())
                 .createdAt(dto.getCreatedAt())
                 .endAt(dto.getEndAt())
                 .thumbnail(dto.getThumbnail())
                 .build();
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
    this.endAt = createdAt.plusMonths(3);
  }

  public void updateCount() {
    this.status.setCount(this.status.getCount() + 1);
  }

  public void updateInfo(SurveyDto dto) {
    this.point = dto.getPoint();
    this.endAt = dto.getEndAt();
    this.createdAt = dto.getCreatedAt();
    this.status.setProgress(dto.getProgress());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
