package com.dns.polinsight.config.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
// TODO: 2021-06-03 :설정 완료 해야함
public class RedisConfig {

  @Value("${spring.redis.host}")
  public String host;

  @Value("${spring.redis.port}")
  public int port;


  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    redisTemplate.setConnectionFactory(connectionFactory);
    return redisTemplate;
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(host);
    configuration.setPort(port);
    return new LettuceConnectionFactory(configuration);
  }

}
