package com.dns.polinsight.service;

import com.dns.polinsight.domain.Point;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

  private final PointRepository repository;


  @Override
  public void saveAndUpdate(Point point) {
    repository.save(point);
  }

  @Override
  public Point getPoint(User user) {
    return repository.findPointByUid(user.getId()).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public Point getHashByEmail(User user) {
    return repository.findPointByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new);
  }

}
