package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.repository.SurveyRepository;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;

  private final SurveyRepository surveyRepository;

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
  public Page<UserDto> adminSearchUserByRegex(String regex, Pageable pageable) {
    Page<User> page = userRepository.findUsersByRegex(regex, pageable);
    List<UserDto> list = page.getContent().stream().map(UserDto::new).collect(Collectors.toList());
    return new PageImpl<>(list, pageable, page.getTotalElements());
  }

  @Override
  public List<Survey> adminSerchSurveyByRegex(String regex, Pageable pageable) {
    return surveyRepository.findSurveysByRegex(regex, pageable);
  }

  @Override
  public long countUserFindRegex(String regex) {
    return userRepository.countUsersByRegex(regex);
  }

  @Override
  public long countSurveyFindRegex(String regex) {
    return surveyRepository.countSurveyByRegex(regex);
  }

}
