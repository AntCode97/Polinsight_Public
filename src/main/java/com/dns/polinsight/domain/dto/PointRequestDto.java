package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.types.BankType;
import com.dns.polinsight.types.PointRequestProgressType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointRequestDto {

  private final LocalDateTime requestedAt = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  private PointRequestProgressType progress;

  private Long id;

  private Long uid;

  @NotNull(message = "요청 포인트는 NULL이 될 수 없습니다")
  @Positive(message = "요청 포인트는 10,000 포인트 이상 설정할 수 있습니다")
  private Long point;

  @NotNull(message = "은행명은 NULL이 될 수 없습니다")
  @Enumerated(EnumType.STRING)
  private BankType bank;

  @NotNull(message = "계좌번호는 NULL이 될 수 없습니다")
  private String account;

  @Setter
  private String email;

  public PointRequestDto(PointRequest pointRequest) {
    this.id = pointRequest.getId();
    this.uid = pointRequest.getUid();
    this.point = pointRequest.getRequestPoint();
    this.account = pointRequest.getAccount();
    this.bank = pointRequest.getBank();
    this.progress = pointRequest.getProgress();
    this.email = pointRequest.getEmail();
  }

}
