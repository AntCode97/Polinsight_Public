package com.dns.polinsight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collector {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long collectorId;

  private String name;

  private String href;

  private Long surveyId;

  private String participateUrl;

  private Long responseCount;

  @JsonIgnore
  @JoinColumn(name = "survey_id")
  @ManyToOne
  private Survey survey;

}
