package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.repository.ParticipateSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipateSurveyServiceImpl implements ParticipateSurveyService {

  private final ParticipateSurveyRepository participateSurveyRepository;


  @Override
  public List<ParticipateSurvey> findByUserId(long userId) {
    return participateSurveyRepository.findParticipateSurveysByUserIdOrderByParticipatedAtDesc(userId);
  }


  @Override
  public List<ParticipateSurvey> findByUserIdAndParticipateAt(long userId, LocalDateTime participateAt) {
    return participateSurveyRepository.findParticipateSurveyByUserIdAndParticipatedAt(userId, participateAt);
  }

  @Override
  public List<ParticipateSurvey> findByUserIdAndSurveyPoint(long userId, long point) {
    return participateSurveyRepository.findParticipateSurveyByUserIdAndSurveyPoint(userId, point);
  }

}
