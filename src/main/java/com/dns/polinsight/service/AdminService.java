package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface AdminService {

  File getExcelFromDB();

  void setPointToSurvey(Survey survey);

  void deleteSurvey(Survey survey);

  void requestPointPayment(User user, Long reqestPoint);

  Page<UserDto> adminSearchUserByRegex(String regex, Pageable pageable);

  List<Survey> adminSerchSurveyByRegex(String regex, Pageable pageable);

  long countUserFindRegex(String regex);

  long countSurveyFindRegex(String regex);

}
