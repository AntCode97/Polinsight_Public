package com.dns.polinsight.repository;

import com.dns.polinsight.domain.ParticipateSurvey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipateSurveyRepository extends JpaRepository<ParticipateSurvey, Long> {

  List<ParticipateSurvey> findParticipateSurveysByUserIdOrderByParticipatedAtDesc(long userId);

  List<ParticipateSurvey> findParticipateSurveyByUserIdAndParticipatedAt(long userId, LocalDateTime participateAt);

  List<ParticipateSurvey> findParticipateSurveyByUserIdAndSurveyPoint(long userId, long surveyPoint);

  @Query(nativeQuery = true, value = "SELECT ps.id              AS id,\n" +
      "       ps.user_id         AS USER,\n" +
      "       ps.survey_id       AS survey,\n" +
      "       ps.participated_at AS participatedAt,\n" +
      "       ps.survey_point    AS surveyPoint,\n" +
      "       ps.hash            AS hash,\n" +
      "       ps.finished        AS finished\n" +
      "FROM (SELECT * FROM survey) AS s,\n" +
      "     participate_survey AS ps\n" +
      "WHERE ps.hash LIKE :hash \n" +
      "  AND ps.survey_id = s.id")
  Optional<ParticipateSurvey> findParticipateSurveyByHash(String hash);

  List<ParticipateSurvey> findAllByUserId(Long userId, Pageable pageable);

  @Modifying
  @Query("update ParticipateSurvey p set p.finished = true where p.id= :id")
  void updateFinishedById(long id);

  List<ParticipateSurvey> findAllByUserId(long userId);

  Boolean existsParticipateSurveyBySurveyId(Long surveyId);

}
