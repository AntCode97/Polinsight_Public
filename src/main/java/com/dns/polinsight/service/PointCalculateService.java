package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointCalculate;

import java.util.List;
import java.util.Optional;

public interface PointCalculateService {

  PointCalculate saveOrUpdate(PointCalculate pointCalculate);

  Optional<PointCalculate> findByPointCalculateByUserId(long userId);

  Optional<PointCalculate> findByPointCalculateById(long id);

  List<PointCalculate> findAllPointCalculates();

}
