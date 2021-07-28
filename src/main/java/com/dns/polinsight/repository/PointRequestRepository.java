package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRequestRepository extends JpaRepository<PointRequest, Long> {

  List<PointRequest> findPointRequestsByUid(long uid);

  Optional<PointRequest> findPointRequestByUidAndRequestPoint(long uid, long requestPoint);

}
