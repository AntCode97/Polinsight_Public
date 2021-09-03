package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.mapper.PointRequestMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointRequestRepository extends JpaRepository<PointRequest, Long> {

  @Query(nativeQuery = true)
  List<PointRequest> findPointRequestsByUid(long uid);

  Optional<PointRequest> findPointRequestByUidAndRequestPoint(long uid, long requestPoint);

  @Query("select pr from PointRequest pr")
  Page<PointRequestMapping> findAllPointRequest(Pageable pageable);

  @Query("select pr from PointRequest pr where pr.email = '%:regex%' or pr.account like '%:regex%' or pr.bank = '%:regex%' or pr.progress = '%:regex%'")
  Page<PointRequestMapping> findAllByRegex(Pageable pageable, String regex);

  long countPointRequestsByUid(Long userId);

}
