package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {


  Optional<PointHistory> findPointHistoryByUserId(long userId);

  Optional<PointHistory> findPointHistoryById(long id);

}
