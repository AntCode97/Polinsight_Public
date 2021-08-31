package com.dns.polinsight.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Survey implements Serializable {

  private static final long serialVersionUID = -9103994299951345908L;

  @Setter
  @Embedded
  @OrderBy("progress ASC")
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  private SurveyStatus status;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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


  @JsonManagedReference
  @OneToOne(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Collector collector;

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
    this.endAt = createdAt.plusMonths(3);
  }


  public void updateCount() {
    this.status.setCount(this.status.getCount() + 1);
  }

}
