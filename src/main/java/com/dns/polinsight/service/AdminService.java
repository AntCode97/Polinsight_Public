package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;

import java.io.File;
import java.util.List;

public interface AdminService {

  File getExcelFromDB();

  void setPointToSurvey(Survey survey);

  void deleteSurvey(Survey survey);

  void requestPointPayment(User user, Long reqestPoint);

  List<User> adminSerchUserByRegex(String regex);

}
