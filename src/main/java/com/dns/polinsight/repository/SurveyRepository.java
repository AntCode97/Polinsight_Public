package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

  List<Survey> findSurveysByTitleLike(String title);

  List<Survey> findSurveysByEndAtLessThan(LocalDateTime endAt);

}
