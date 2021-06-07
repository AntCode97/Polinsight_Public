package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;

import java.util.List;
import java.util.Optional;


public interface SurveyService {
  Survey save(Survey survey);

  List<Survey> findAll();

  Survey findById(Survey survey);

  Optional<Survey> update(Survey survey);

  void delete(Survey survey);
}
