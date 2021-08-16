package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.dto.SurveyDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

  List<Survey> findSurveysByTitleLike(String title, Pageable pageable);

  List<Survey> findSurveysByEndAtLessThan(LocalDateTime endAt);

  @Query(nativeQuery = true, value = "SELECT * FROM survey WHERE survey_id LIKE %?1% OR progress_type LIKE %?1% OR point LIKE %?1% OR title LIKE %?1%")
  List<Survey> findSurveysByRegex(String regex, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM survey WHERE survey_id LIKE %?1% OR progress_type LIKE %?1% OR point LIKE %?1% OR title LIKE %?1%")
  long countSurveyByRegex(String regex);

  Optional<Survey> findSurveyBySurveyId(long surveyId);

  @Query(nativeQuery = true, value = "UPDATE survey SET point = ?2 , created_at = ?3, end_at  = ?4, progress = ?5  WHERE id = ?1")
  void adminSurveyUpdate(long id, long point, String createdAt, String endAt, String progress);


  @Query(nativeQuery = true, value = "SELECT s.title AS title, s.point AS point, s.survey_id AS surveyId, s.progress AS progress, s.minimum_time AS minimumTime, s.created_at AS createdAt, s.end_at " +
      "AS endAt, c.participate_url AS participateUrl, s.question_count AS count FROM survey s JOIN collector c ON s.survey_id = c.survey_id;")
  List<SurveyDto> findAllSurveyWithCollector();

}