package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.repository.ParticipateSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipateSurveyServiceImpl implements ParticipateSurveyService {

  private final ParticipateSurveyRepository participateSurveyRepository;


  @Override
  public List<ParticipateSurvey> findByUserId(long userId) {
    return participateSurveyRepository.findParticipateSurveysByUserIdOrderByParticipatedAtDesc(userId);
  }

  @Override
  public Optional<ParticipateSurvey> findBySurveyUserPairHash(String hash) {
    return participateSurveyRepository.findParticipateSurveyByHash(hash);
  }

  @Override
  public List<ParticipateSurvey> findByUserIdAndParticipateAt(long userId, LocalDateTime participateAt) {
    return participateSurveyRepository.findParticipateSurveyByUserIdAndParticipatedAt(userId, participateAt);
  }

  @Override
  public List<ParticipateSurvey> findByUserIdAndSurveyPoint(long userId, long point) {
    return participateSurveyRepository.findParticipateSurveyByUserIdAndSurveyPoint(userId, point);
  }

  @Override
  public ParticipateSurvey saveParticipateSurvey(ParticipateSurvey participateSurvey) {
    return participateSurveyRepository.save(participateSurvey);
  }

  @Override
  public ParticipateSurvey getHashByEmail(String email) {
    return null;
  }

  @Override
  public ParticipateSurvey getPoint(User user) {
    return null;
  }


  @Override
  public ParticipateSurvey getParticipateSurveyByUserId(long userId) {
    return null;
  }

  @Override
  public ParticipateSurvey getParticipateSurveyByUserEmail(String email) {
    return null;
  }


  @Override
  public void saveAndUpdate(ParticipateSurvey participateSurvey) {

  }

}
