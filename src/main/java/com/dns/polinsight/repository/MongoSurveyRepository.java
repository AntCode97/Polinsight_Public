package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoSurveyRepository extends MongoRepository<Survey, Long> {


}
