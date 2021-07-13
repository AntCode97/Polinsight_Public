package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository {


  Survey save(Survey survey);

  List<Survey> findAll();

  Survey findById(Survey survey);

  Optional<Survey> update(Survey survey);

  void delete(Survey survey);

}
