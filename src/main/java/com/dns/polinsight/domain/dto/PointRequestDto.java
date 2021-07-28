package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.types.BankType;
import com.dns.polinsight.types.PointRequestProgressType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointRequestDto {

  private final LocalDateTime requestedAt = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  private PointRequestProgressType progressType;

  private Long uid;

  @NotNull
  private Long requestPoint;

  @NotNull
  @Enumerated(EnumType.STRING)
  private BankType bankName;

  @NotNull
  private String account;

  public PointRequestDto(PointRequest pointRequest) {
    this.requestPoint = pointRequest.getRequestPoint();
    this.account = pointRequest.getAccount();
    this.bankName = pointRequest.getBankName();
    this.progressType = pointRequest.getProgressType();
  }

}
