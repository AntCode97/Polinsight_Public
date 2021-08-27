package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointHistory;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PointHistoryService {

  PointHistory saveOrUpdate(PointHistory pointHistory);

  Optional<PointHistory> findByPointHistoryByUserId(long userId);

  Optional<PointHistory> findByPointHistoryById(long id);

  List<PointHistory> findAllPointHistorys();

  List<PointHistory> findAllPointHistoryByUserId(long userId, Pageable pageable);

  List<PointHistory> findAllPointHistoryByUserId(long userId);

  List<PointHistory> findAll();

}
