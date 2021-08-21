package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointRequestRepository extends JpaRepository<PointRequest, Long> {

  @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM account LIKE %?1% OR requested_at LIKE %?1% OR bank_name LIKE %?1% OR request_point LIKE %?1%")
  long countPointRequestsByRegex(String regex);

  @Query(nativeQuery = true, value = "SELECT * FROM account LIKE %?1% OR requested_at LIKE %?1% OR bank_name LIKE %?1% OR request_point LIKE %?1%")
  Page<PointRequest> findPointRequestsByRegex(Pageable pageable, String regex);

  @Query(nativeQuery = true)
  List<PointRequest> findPointRequestsByUid(long uid);

  Optional<PointRequest> findPointRequestByUidAndRequestPoint(long uid, long requestPoint);

}
