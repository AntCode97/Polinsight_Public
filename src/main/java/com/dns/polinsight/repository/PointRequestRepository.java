package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.projection.PointRequestMapping;
import com.dns.polinsight.types.PointRequestProgressType;
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

  @Query("select pr from PointRequest pr where pr.progress = 'REQUESTED' and (pr.email = :regex or pr.account like %:regex% or pr.bank = :regex)")
  Page<PointRequestMapping> findAllByRegex(Pageable pageable, String regex);

  @Query("select pr from PointRequest pr where pr.progress =:type")
  Page<PointRequestMapping> findAllPointRequestAndType(Pageable pageable, PointRequestProgressType type);

  @Query("select pr from PointRequest pr where pr.progress = :type and (pr.email = :regex or pr.account like :regex or pr.bank = :regex)")
  Page<PointRequestMapping> findAllByRegexAndType(Pageable pageable, String regex, PointRequestProgressType type);

  long countPointRequestsByUid(Long userId);

}
