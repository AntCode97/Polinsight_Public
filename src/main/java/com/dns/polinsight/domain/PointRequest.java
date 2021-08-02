package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.types.BankType;
import com.dns.polinsight.types.PointRequestProgressType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/*
 * 사용자가 포인트를 현금화 시키고자 할 때, 사용할 테이블
 * */
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long uid;

  private Long requestPoint;

  private LocalDateTime requestedAt;

  private BankType bankName;

  private String account;

  @Setter
  private PointRequestProgressType progressType;

  public PointRequest of(PointRequestDto dto) {
    this.uid = dto.getUid();
    this.requestPoint = dto.getPoint();
    this.requestedAt = dto.getRequestedAt();
    this.bankName = dto.getBank();
    this.account = dto.getAccount();
    this.progressType = dto.getProgress();
    return this;
  }

}
