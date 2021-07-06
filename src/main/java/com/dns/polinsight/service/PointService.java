package com.dns.polinsight.service;


import com.dns.polinsight.domain.Point;
import com.dns.polinsight.domain.User;

public interface PointService {

  void saveAndUpdate(Point point);

  Point getPoint(User user);


  Point getHashByEmail(User user);

}
