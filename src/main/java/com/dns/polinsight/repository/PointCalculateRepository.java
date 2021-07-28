package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointCalculate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointCalculateRepository extends JpaRepository<PointCalculate, Long> {


  Optional<PointCalculate> findPointCalculateByUid(long userId);

  Optional<PointCalculate> findPointCalculateBySeq(long seq);

}
