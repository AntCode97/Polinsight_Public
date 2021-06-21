package com.dns.polinsight.config.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
//@Configuration
public class MongoDBConfig {

//  private final String dbName = "polinsight";

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create(mongoUri);
  }

}
