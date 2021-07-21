package com.dns.polinsight.service;

import com.dns.polinsight.domain.Point;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.repository.PointRepository;
import com.dns.polinsight.repository.PointRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

  final String salt = "polinsightPointSalt";

  private final PointRepository repository;

  private final PointRequestRepository pointRequestRepository;


  @Override
  public void saveAndUpdate(Point point) {
    repository.save(point);
  }

  @Override
  public Point getPoint(User user) {
    return repository.findPointByUid(user.getId()).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public String getHash(String email) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();
    digest.update(salt.getBytes(StandardCharsets.UTF_8));
    digest.update(email.getBytes(StandardCharsets.UTF_8));
    return String.format("%0128x", new BigInteger(1, digest.digest()));
  }

  @Override
  public Point getHashByEmail(User user) {
    return repository.findPointByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public PointRequest addUserPointRequest(Long uid, Long point) {
    return pointRequestRepository.save(PointRequest.builder()
                                            .uid(uid)
                                            .requestedAt(LocalDateTime.now())
                                            .requestPoint(point)
                                            .build());
  }

  @Override
  public List<PointRequest> getUserPointRequest(Long uid) {
    return pointRequestRepository.findPointRequestsByUid(uid);
  }

}
