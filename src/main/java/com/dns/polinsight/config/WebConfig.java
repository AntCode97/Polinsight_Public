package com.dns.polinsight.config;

import com.dns.polinsight.config.resolver.CurrentUserResolver;
import com.dns.polinsight.types.convereter.EmailConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  //"file:///Users/yunjun/IntelliJProjects/polinsight/"
  static String[] resourceLocations = {"classpath:/templates/", "classpath:/static/", "file:///Users/yunjun/IntelliJProjects/polinsight/"};

  private final CurrentUserResolver currentUserResolver;

  @Value("${file.upload.baseLocation}")
  private String baseLocation;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
            .addResourceLocations(resourceLocations);
    //    registry.addResourceHandler("/upload-dir/**")
    //            .addResourceLocations("/Users/yunjun/IntelliJProjects/polinsight/", "/Users/yunjun/IntelliJProjects/polinsight/upload-dir/","classpath:/../../../upload-dir/");
    //    registry.addResourceHandler("/**")
    //            .addResourceLocations("file:///Users/yunjun/IntelliJProjects/polinsight/");
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

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserResolver);
  }


}
