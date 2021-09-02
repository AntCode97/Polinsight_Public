package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.repository.ParticipateSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipateSurveyServiceImpl implements ParticipateSurveyService {

  private final ParticipateSurveyRepository participateSurveyRepository;


  @Override
  public List<ParticipateSurvey> findAll() {
    return participateSurveyRepository.findAll();
  }

  @Override
  public List<ParticipateSurvey> findAllByUserId(long userId, Pageable pageable) {
    return participateSurveyRepository.findAllByUserId(userId, pageable);
  }

  public List<ParticipateSurvey> findAllByUserId(long userId) {
    return participateSurveyRepository.findAllByUserId(userId);
  }

  @Override
  public Optional<ParticipateSurvey> findBySurveyUserPairHash(String hash) {
    return participateSurveyRepository.findParticipateSurveyByHash(hash);
  }

  @Override
  public long countExistParticipateSurvey() {
    return participateSurveyRepository.count();
  }

  @Override
  public ParticipateSurvey saveParticipateSurvey(ParticipateSurvey participateSurvey) {
    return participateSurveyRepository.save(participateSurvey);
  }

  @Override
  public ParticipateSurvey getParticipateSurveyByUserEmail(String email) {
    return null;
  }


  @Override
  public ParticipateSurvey saveAndUpdate(ParticipateSurvey participateSurvey) {
    return participateSurveyRepository.saveAndFlush(participateSurvey);
  }


  @Override
  public void updateParticipateSurveyById(long pSurveyId) {
    participateSurveyRepository.updateFinishedById(pSurveyId);
  }

}
