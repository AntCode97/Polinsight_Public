package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.repository.ParticipateSurveyRepository;
import com.dns.polinsight.repository.PointRequestRepository;
import com.dns.polinsight.repository.UserRepository;
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

  private final ParticipateSurveyRepository participateSurveyRepository;

  private final UserRepository userRepository;


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
  public long countAllPointRequests() {
    return pointRequestRepository.count();
  }

  @Override
  public long countPointRequestsByRegex(String regex) {
    return pointRequestRepository.countPointRequestsByRegex(regex);
  }


  @Override
  public Page<PointRequestDto> getAllPointRequests(Pageable pageable) {
    return pointRequestRepository.findAllPointRequest(pageable);
  }

  @Override
  public Page<PointRequest> getAllPointRequestsByRegex(Pageable pageable, String regex) {
    return pointRequestRepository.findPointRequestsByRegex(pageable, regex);
  }

  @Override
  public void deletePointRequestById(long id) {
    pointRequestRepository.deleteById(id);
  }


  @Override
  public List<PointRequest> findAll() {
    return pointRequestRepository.findAll();
  }

}
