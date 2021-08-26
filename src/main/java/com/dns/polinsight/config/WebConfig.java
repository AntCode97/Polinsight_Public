package com.dns.polinsight.config;

import com.dns.polinsight.types.convereter.EmailConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  static String[] resourceLocations = {"classpath:/templates/", "classpath:/static/"};

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
            .addResourceLocations(resourceLocations);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*")
            .maxAge(60 * 60);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new EmailConverter.StringToEmailConverter());
    registry.addConverter(new EmailConverter.EmailToStringConverter());
  }

}
