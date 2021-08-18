package com.dns.polinsight.service;

import com.dns.polinsight.domain.Collector;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.repository.CollectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {

  private final CollectorRepository collectorRepository;


  private List<Long> getCollectorIdListBySurveyId(Long surveyId) {
    return collectorRepository.findCollectorsBySurvey(Survey.builder().id(surveyId).build()).parallelStream().map(Collector::getId).collect(Collectors.toList());
  }

  @Override
  public long countParticipantBySurveyId(Long surveyId) {
    return collectorRepository.findCollectorsBySurvey(Survey.builder().surveyId(surveyId).build()).parallelStream().mapToLong(Collector::getResponseCount).reduce(0L, Long::sum);
  }

  @Override
  public Collector saveCollector(Collector collector) {
    return collectorRepository.save(collector);
  }

  @Override
  public Collector updateCollector(Collector collector) {
    return collectorRepository.saveAndFlush(collector);
  }

  @Override
  public List<Collector> findCollectorBySurveyId(Long surveyId) {
    return collectorRepository.findCollectorsBySurvey(Survey.builder().id(surveyId).build());
  }

  @Override
  public Optional<Collector> findCollectorById(Long id) {
    return collectorRepository.findById(id);
  }

  @Override
  public void deleteCollectorById(Long id) {
    collectorRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteCollectorBySurveyId(Long surveyId) {
    collectorRepository.deleteAllById(this.getCollectorIdListBySurveyId(surveyId));
  }

}
