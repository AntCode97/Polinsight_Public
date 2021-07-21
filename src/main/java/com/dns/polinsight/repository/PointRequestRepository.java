package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRequestRepository extends JpaRepository<PointRequest, Long> {

  List<PointRequest> findPointRequestsByUid(Long uid);

}
