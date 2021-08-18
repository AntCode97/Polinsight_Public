package com.dns.polinsight.repository;


import com.dns.polinsight.domain.Collector;
import com.dns.polinsight.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectorRepository extends JpaRepository<Collector, Long> {

  //  List<Collector> findCollectorsByCollectorId(Long collectorId);

  List<Collector> findCollectorsBySurvey(Survey survey);

  //  List<Collector> findAllByCollectorId(List<Long> collectorIdList);

}
