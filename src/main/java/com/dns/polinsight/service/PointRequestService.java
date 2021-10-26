package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.types.PointRequestProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PointRequestService {


  PointRequest saveOrUpdate(PointRequest pointRequest);

  List<PointRequest> getUserPointRequests(long uid);

  Optional<PointRequest> addUserPointRequest(long uid, long point);

  Optional<PointRequest> findPointRequestById(long id);

  Page<PointRequest> findAllPointRequests(Pageable pageable);

  Page<PointRequest> findAllPointRequestsByRegex(Pageable pageable, String regex);

  Page<PointRequest> findAllPointRequestsAndType(Pageable pageable, PointRequestProgressType type);

  Page<PointRequest> findAllPointRequestsByRegexAndType(Pageable pageable, String regex, PointRequestProgressType type);

  Page<PointRequest> findAllOngoingRequest(Pageable pageable);

  Page<PointRequest> findAllOngoingRequestByRegex(Pageable pageable, String regex);

  void deletePointRequestById(long id);


  List<PointRequest> findAll();

  long countExistsPointRequests();

  long countExistsPointRequestsByUserId(Long userId);

}
