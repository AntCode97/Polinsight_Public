package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final MongoTemplate mongoTemplate;

  @Override
  public Survey save(Survey survey) {
    return null;
  }

  @Cacheable
  @Override
  public List<Survey> findAll() {
    return null;
  }

  @Cacheable
  @Override
  public Survey findById(Survey survey) {
    return null;
  }

  @Override
  public Optional<Survey> update(Survey survey) {
    return Optional.empty();
  }

  @Override
  public void delete(Survey survey) {

  }

}
