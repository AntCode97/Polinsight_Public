package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.types.BankType;
import com.dns.polinsight.types.PointRequestProgressType;
import lombok.*;

import javax.persistence.*;
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

  @Setter
  private Long uid;

  private String email;

  private Long requestPoint;

  private LocalDateTime requestedAt;

  @Enumerated(EnumType.STRING)
  private BankType bank;

  private String account;

  @Setter
  @Enumerated(EnumType.STRING)
  private PointRequestProgressType progress;

  public PointRequest of(PointRequestDto dto) {
    this.requestedAt = dto.getRequestedAt();
    this.bank = dto.getBank();
    this.account = dto.getAccount();
    this.progress = dto.getProgress();
    return this;
  }

  public PointRequest progressUpdate(PointRequestDto dto) {
    if (dto.getProgress() == PointRequestProgressType.REQUESTED) {
      this.progress = PointRequestProgressType.PROCESSING;
    } else if (dto.getProgress() == PointRequestProgressType.PROCESSING) {
      this.progress = PointRequestProgressType.FINISHED;
    }
    return this;
  }

}
