package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SurveyRepositoryImpl implements SurveyRepository {

  private final MongoTemplate template;

  @Override
  public Survey save(Survey survey) {
    return template.save(survey);
  }

  @Override
  public List<Survey> findAll() {
    return template.findAll(Survey.class);
  }

  @Override
  public Survey findById(Survey survey) {
    return template.findById(survey.getId(), Survey.class);
  }

  @Override
  public Optional<Survey> update(Survey survey) {
    return null;
  }

  @Override
  public void delete(Survey survey) {
    template.remove(survey);
  }

}
