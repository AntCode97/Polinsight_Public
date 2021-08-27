package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

  private final PointHistoryRepository pointHistoryRepository;

  @Override
  public PointHistory saveOrUpdate(PointHistory PointHistory) {
    return pointHistoryRepository.save(PointHistory);
  }

  @Override
  public Optional<PointHistory> findByPointHistoryByUserId(long userId) {
    return pointHistoryRepository.findPointHistoryByUserId(userId);
  }

  @Override
  public Optional<PointHistory> findByPointHistoryById(long id) {
    return pointHistoryRepository.findPointHistoryById(id);
  }

  @Override
  public List<PointHistory> findAllPointHistorys() {
    return pointHistoryRepository.findAll();
  }

  @Override
  public List<PointHistory> findAllPointHistoryByUserId(long userId, Pageable pageable) {
    return pointHistoryRepository.findPointHistoriesByUserId(userId, pageable);
  }

  @Override
  public List<PointHistory> findAllPointHistoryByUserId(long userId) {
    return pointHistoryRepository.findAllByUserId(userId);
  }

  @Override
  public List<PointHistory> findAll() {
    return pointHistoryRepository.findAll();
  }

}
