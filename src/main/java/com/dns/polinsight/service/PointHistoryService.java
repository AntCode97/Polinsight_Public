package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointHistory;

import java.util.List;
import java.util.Optional;

public interface PointHistoryService {

  PointHistory saveOrUpdate(PointHistory pointHistory);

  Optional<PointHistory> findByPointHistoryByUserId(long userId);

  Optional<PointHistory> findByPointHistoryById(long id);

  List<PointHistory> findAllPointHistorys();

}
