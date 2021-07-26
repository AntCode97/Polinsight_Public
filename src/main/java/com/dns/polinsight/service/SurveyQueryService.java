package com.dns.polinsight.service;


import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.SurveyQuery;
import com.dns.polinsight.domain.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface SurveyQueryService {

  void saveAndUpdate(SurveyQuery point);

  SurveyQuery getPoint(User user);

  String getHash(String email) throws NoSuchAlgorithmException;

  SurveyQuery getHashByEmail(User user);

  PointRequest addUserPointRequest(Long uid, Long point);

  List<PointRequest> getUserPointRequests(Long uid);


}
