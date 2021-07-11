package com.dns.polinsight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/*
 * Swagger 3.0 버전 이후부터는 SpringBoot에 의해 자동 설정되므로
 * @EnableSwagger2 어노테이션의 선언이 필요 없다.
 * 바로 Docket을 작성해주자
 * */
@Configuration
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
        .select()
        .apis(RequestHandlerSelectors.any())  // TODO: 2021-06-16 0016 추후 설정 필요
        .paths(PathSelectors.any())   // TODO: 2021-06-16 0016 추후 설정 필요
        .build();
  }

}
