package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.projection.SurveyMapping;
import com.dns.polinsight.types.ProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

  String surveyJoinData = "select s.id as id, s.title as title, s.point as point, s.surveyId as surveyId, s.status.progress as progress, s.status.minimumTime as minimumTime, s.createdAt as " +
      "createdAt, s.endAt as endAt, s.thumbnail as thumbnail, s.questionCount as questionCount, c.participateUrl as participateUrl, s.originalName as originalName";

  Page<Survey> findAllByTitleLikeOrderById(Pageable pageable, String title);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId=c.survey.surveyId where s.id = :id")
  SurveyMapping findSurveyMappingById(long id);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId = c.survey.surveyId")
  List<SurveyMapping> findAllSurveyMapping();

  @Query(nativeQuery = true, value = "SELECT * FROM survey WHERE survey_id LIKE %?1% OR progress_type LIKE %?1% OR point LIKE %?1% OR title LIKE %?1%")
  List<Survey> findSurveysByRegex(String regex, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM survey WHERE survey_id LIKE %?1% OR progress_type LIKE %?1% OR point LIKE %?1% OR title LIKE %?1%")
  long countSurveyByRegex(String regex);

  @Query(nativeQuery = true, value = "UPDATE survey SET point = ?2 , created_at = ?3, end_at  = ?4, progress = ?5  WHERE id = ?1")
  int adminSurveyUpdate(long id, long point, String createdAt, String endAt, String progress);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId = c.survey.surveyId where s.status.progress = :type")
  Page<SurveyMapping> findAllByTypes(ProgressType type, Pageable pageable);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId = c.survey.surveyId")
  Page<SurveyMapping> findAllSurveys(Pageable pageable);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId = c.survey.surveyId where s.status.progress = :progress and (s.title like %:regex% or s.point = :regexL or s.status.count = :regexL " +
      "or s.questionCount = :regexL) ")
  Page<SurveyMapping> findAllByStatusProgressByRegex(ProgressType progress, String regex, Long regexL, Pageable pageable);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId = c.survey.surveyId where s.point = :regexL or s.status.count = :regexL or s.questionCount" +
      " = :regexL or s.title like %:regex% ")
  Page<SurveyMapping> findAllByRegex(String regex, Long regexL, Pageable pageable);

  @Query(surveyJoinData + " from Survey s left join fetch Collector c on s.surveyId = c.survey.surveyId where s.status.progress <> :type ")
  Page<SurveyMapping> findByProgressTypeNotLike(ProgressType type, Pageable pageable);


}