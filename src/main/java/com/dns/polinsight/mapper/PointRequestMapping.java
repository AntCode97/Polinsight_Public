package com.dns.polinsight.mapper;

import com.dns.polinsight.types.BankType;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.PointRequestProgressType;

import java.time.LocalDateTime;

public interface PointRequestMapping {

  Long getId();

  Long getRequestPoint();

  LocalDateTime getRequestedAt();

  BankType getBank();

  String getAccount();

  Email getEmail();

  PointRequestProgressType getProgress();

}
