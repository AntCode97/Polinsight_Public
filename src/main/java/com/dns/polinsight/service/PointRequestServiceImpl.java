package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.repository.PointRequestRepository;
import com.dns.polinsight.types.PointRequestProgressType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRequestServiceImpl implements PointRequestService {

  private final PointRequestRepository pointRequestRepository;

  @Override
  public PointRequest saveOrUpdate(PointRequest pointRequest) {
    return pointRequestRepository.save(pointRequest);
  }

  @Override
  public List<PointRequest> getUserPointRequests(long uid) {
    return pointRequestRepository.findPointRequestsByUid(uid);
  }

  @Override
  public Optional<PointRequest> addUserPointRequest(long uid, long point) {
    return pointRequestRepository.findPointRequestByUidAndRequestPoint(uid, point);
  }

  @Override
  public Optional<PointRequest> findPointRequestById(long id) {
    return pointRequestRepository.findById(id);
  }


  @Override
  public Page<PointRequest> findAllPointRequests(Pageable pageable) {
    return pointRequestRepository.findAll(pageable);
  }

  @Override
  public Page<PointRequest> findAllPointRequestsByRegex(Pageable pageable, String regex) {
    return pointRequestRepository.findAllByRegex(pageable, regex);
  }

  @Override
  public Page<PointRequest> findAllPointRequestsAndType(Pageable pageable, PointRequestProgressType type) {
    return pointRequestRepository.findAllPointRequestAndType(pageable, type.name());
  }

  @Override
  public Page<PointRequest> findAllPointRequestsByRegexAndType(Pageable pageable, String regex, PointRequestProgressType type) {
    return pointRequestRepository.findAllByRegexAndType(pageable, regex, type.name());
  }

  @Override
  public Page<PointRequest> findAllOngoingRequest(Pageable pageable) {
    return pointRequestRepository.findAllOngoingRequest(pageable);
  }

  @Override
  public Page<PointRequest> findAllOngoingRequestByRegex(Pageable pageable, String regex) {
    return pointRequestRepository.findAllOngoingRequestByRegex(pageable, regex);
  }

  @Override
  public void deletePointRequestById(long id) {
    pointRequestRepository.deleteById(id);
  }


  @Override
  public List<PointRequest> findAll() {
    return pointRequestRepository.findAll();
  }

  @Override
  public long countExistsPointRequests() {
    return pointRequestRepository.count();
  }

  @Override
  public long countExistsPointRequestsByUserId(Long userId) {
    return pointRequestRepository.countPointRequestsByUid(userId);
  }

}
