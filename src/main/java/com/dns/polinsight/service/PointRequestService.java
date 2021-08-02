package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PointRequestService {


  PointRequest saveOrUpdate(PointRequest pointRequest);

  List<PointRequest> getUserPointRequests(long uid);

  Optional<PointRequest> addUserPointRequest(long uid, long point);

  Optional<PointRequest> findPointRequestById(long id);

  long countAllPointRequests();

  long countPointRequestsByRegex(String regex);

  Page<PointRequest> getAllPointRequests(Pageable pageable);

  Page<PointRequest> getAllPointRequestsByRegex(Pageable pageable, String regex);

}
