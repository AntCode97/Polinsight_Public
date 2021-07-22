package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;

  @Override
  public File getExcelFromDB() {
    return null;
  }

  @Override
  public void setPointToSurvey(Survey survey) {

  }

  @Override
  public void deleteSurvey(Survey survey) {

  }

  @Override
  public void requestPointPayment(User user, Long reqestPoint) {

  }

  @Override
  public List<User> adminSerchUserByRegex(String regex) {
    return userRepository.findUsersByRegex(regex);
  }

}
