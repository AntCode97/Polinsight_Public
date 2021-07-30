package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface AdminService {

  File getExcelFromDB();

  void setPointToSurvey(Survey survey);

  void deleteSurvey(Survey survey);

  void requestPointPayment(User user, Long reqestPoint);

  List<User> adminSerchUserByRegex(String regex, Pageable pageable);

  List<Survey> adminSerchSurveyByRegex(String regex, Pageable pageable);

  long countUserFindRegex(String regex);

  long countSurveyFindRegex(String regex);

}
