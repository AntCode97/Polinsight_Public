package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

  Optional<Point> findPointByUid(Long uid);

  Optional<Point> findPointByEmail(String email);

}
