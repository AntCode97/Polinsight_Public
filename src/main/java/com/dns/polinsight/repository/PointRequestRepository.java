package com.dns.polinsight.repository;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.types.PointRequestProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointRequestRepository extends JpaRepository<PointRequest, Long> {

  List<PointRequest> findPointRequestsByUid(long uid);

  Optional<PointRequest> findPointRequestByUidAndRequestPoint(long uid, long requestPoint);

  @Query(nativeQuery = true,
      value = "SELECT * FROM (SELECT * FROM point_request WHERE progress = 'REQUESTED') AS pr " +
          "WHERE pr.email LIKE %:regex% OR pr.account LIKE %:regex% OR pr.bank LIKE %:regex%")
  Page<PointRequest> findAllByRegex(Pageable pageable, String regex);


  @Query(nativeQuery = true,
      value = "SELECT * FROM point_request WHERE progress = :type")
  Page<PointRequest> findAllPointRequestAndType(Pageable pageable, String type);

  @Query(nativeQuery = true,
      value = "SELECT * FROM (SELECT * FROM point_request WHERE progress = :type) AS pr " +
          "WHERE pr.email LIKE %:regex% OR pr.account LIKE %:regex% OR pr.bank LIKE %:regex%)")
  Page<PointRequest> findAllByRegexAndType(Pageable pageable, String regex, String type);

  @Query(nativeQuery = true,
      value = "SELECT * FROM point_request WHERE progress = 'REQUESTED' OR progress = 'WAIT' OR progress = 'ERROR'")
  Page<PointRequest> findAllOngoingRequest(Pageable pageable);

  @Query(nativeQuery = true,
      value = "SELECT * FROM " +
          "(SELECT * FROM point_request WHERE progress = 'REQUESTED' OR progress = 'WAIT' OR progress = 'ERROR') AS pr " +
          "WHERE pr.email LIKE %:regex% OR pr.account LIKE %:regex% OR pr.bank LIKE %:regex%")
  Page<PointRequest> findAllOngoingRequestByRegex(Pageable pageable, String regex);

  long countPointRequestsByUid(Long userId);

}
