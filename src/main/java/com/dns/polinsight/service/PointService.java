package com.dns.polinsight.service;


import com.dns.polinsight.domain.Point;
import com.dns.polinsight.domain.User;

import java.security.NoSuchAlgorithmException;

public interface PointService {

  void saveAndUpdate(Point point);

  Point getPoint(User user);

  String getHash(String email) throws NoSuchAlgorithmException;

  Point getHashByEmail(User user);

}
