package com.dns.polinsight.service;


import com.dns.polinsight.domain.Collector;

import java.util.List;
import java.util.Optional;

public interface CollectorService {

  long countParticipantBySurveyId(Long surveyId);

  Collector saveCollector(Collector collector);

  Collector updateCollector(Collector collector);

  List<Collector> findCollectorBySurveyId(Long surveyId);

  Optional<Collector> findCollectorById(Long id);

  void deleteCollectorById(Long id);

  void deleteCollectorBySurveyId(Long surveyId);

}
