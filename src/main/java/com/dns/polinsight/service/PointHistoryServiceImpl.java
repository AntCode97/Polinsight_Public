package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

  private final PointHistoryRepository PointHistoryRepository;

  @Override
  public PointHistory saveOrUpdate(PointHistory PointHistory) {
    return PointHistoryRepository.save(PointHistory);
  }

  @Override
  public Optional<PointHistory> findByPointHistoryByUserId(long userId) {
    return PointHistoryRepository.findPointHistoryByUid(userId);
  }

  @Override
  public Optional<PointHistory> findByPointHistoryById(long id) {
    return PointHistoryRepository.findPointHistoryById(id);
  }

  @Override
  public List<PointHistory> findAllPointHistorys() {
    return PointHistoryRepository.findAll();
  }

}
