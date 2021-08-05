package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointCalculate;
import com.dns.polinsight.repository.PointCalculateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointCalculateServiceImpl implements PointCalculateService {

  private final PointCalculateRepository pointCalculateRepository;

  @Override
  public PointCalculate saveOrUpdate(PointCalculate pointCalculate) {
    return pointCalculateRepository.save(pointCalculate);
  }

  @Override
  public Optional<PointCalculate> findByPointCalculateByUserId(long userId) {
    return pointCalculateRepository.findPointCalculateByUid(userId);
  }

  @Override
  public Optional<PointCalculate> findByPointCalculateById(long id) {
    return pointCalculateRepository.findPointCalculateBySeq(id);
  }

  @Override
  public List<PointCalculate> findAllPointCalculates() {
    return pointCalculateRepository.findAll();
  }

}
