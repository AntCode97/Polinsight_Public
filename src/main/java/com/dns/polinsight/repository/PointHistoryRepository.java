package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {


  Optional<PointHistory> findPointHistoryByUserId(long userId);

  Optional<PointHistory> findPointHistoryById(long id);

  List<PointHistory> findPointHistoriesByUserId(long userId, Pageable pageable);

  List<PointHistory> findAllByUserId(long userId);

  List<PointHistory> findAllByUser(User user);

}
