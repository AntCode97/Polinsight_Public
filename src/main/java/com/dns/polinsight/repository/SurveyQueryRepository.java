package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Point;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.SurveyQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyQueryRepository extends JpaRepository<SurveyQuery, Long> {

  Optional<SurveyQuery> findPointByUid(Long uid);

  Optional<SurveyQuery> findPointByEmail(String email);

}
