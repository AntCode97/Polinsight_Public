package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.dto.SurveyDto;
import com.dns.polinsight.types.ProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

  @Query("select s from Survey s where s.status.progress = :progress and (s.title like :regex or s.point = :regex or s.status.count = :regex or s.questionCount = :regex or " +
      "s.createdAt = :regex or s.endAt = :regex)")
  Page<SurveyDto> findAllByStatusProgressByRegex(ProgressType progress, String regex, Pageable pageable);

  @Query("select s from Survey s where s.title like :regex or s.point = :regex or s.status.count = :regex or s.questionCount = :regex or " +
      "s.createdAt = :regex or s.endAt = :regex")
  Page<SurveyDto> findAllByRegex(String regex, Pageable pageable);

  Page<Survey> findAllByTitleLikeOrderById(Pageable pageable, String title);

  List<Survey> findSurveysByTitleLike(String title, Pageable pageable);

  List<Survey> findSurveysByEndAtLessThan(LocalDate endAt);

  @Query(nativeQuery = true, value = "SELECT * FROM survey WHERE survey_id LIKE %?1% OR progress_type LIKE %?1% OR point LIKE %?1% OR title LIKE %?1%")
  List<Survey> findSurveysByRegex(String regex, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM survey WHERE survey_id LIKE %?1% OR progress_type LIKE %?1% OR point LIKE %?1% OR title LIKE %?1%")
  long countSurveyByRegex(String regex);

  Optional<Survey> findSurveyBySurveyId(long surveyId);

  @Query(nativeQuery = true, value = "UPDATE survey SET point = ?2 , created_at = ?3, end_at  = ?4, progress = ?5  WHERE id = ?1")
  void adminSurveyUpdate(long id, long point, String createdAt, String endAt, String progress);

  @Query(nativeQuery = true, value = "SELECT COUNT(s.title) FROM survey s JOIN collector c ON s.survey_id = c.survey_id")
  long countAllSurveyWithCollector();

  @Query(nativeQuery = true, value = "SELECT COUNT(s.title) FROM survey s JOIN collector c ON s.survey_id = c.survey_id WHERE s.progress LIKE ?1")
  long countAllSurveyWithCollectorWithCondition(String condition);

}