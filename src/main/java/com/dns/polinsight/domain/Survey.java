package com.dns.polinsight.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@ToString
public class Survey implements Serializable {


  @Setter
  @Embedded
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  private SurveyStatus status;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "survey_id")
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

  @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Collector> collector;

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
    this.endAt = createdAt.plusMonths(3);
  }


}
