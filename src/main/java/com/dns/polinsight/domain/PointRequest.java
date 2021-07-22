package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {

  @Id
  private Long id;

  private Long uid;

  private Long requestPoint;

  private LocalDateTime requestedAt;

  private String account;

}
