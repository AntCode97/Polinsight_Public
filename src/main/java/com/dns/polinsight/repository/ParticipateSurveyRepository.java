package com.dns.polinsight.repository;

import com.dns.polinsight.domain.ParticipateSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipateSurveyRepository extends JpaRepository<ParticipateSurvey, Long> {

  List<ParticipateSurvey> findParticipateSurveysByUserIdOrderByParticipatedAtDesc(long userId);

  List<ParticipateSurvey> findParticipateSurveyByUserIdAndParticipatedAt(long userId, LocalDateTime participateAt);

  List<ParticipateSurvey> findParticipateSurveyByUserIdAndSurveyPoint(long userId, long surveyPoint);

  Optional<ParticipateSurvey> findParticipateSurveyByHash(String hash);

  List<ParticipateSurvey> findAllByUserId(Long userId);

}
