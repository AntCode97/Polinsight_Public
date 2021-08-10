package com.dns.polinsight.config;

import com.dns.polinsight.config.oauth.LoginUserArgumentResolver;
import com.dns.polinsight.utils.PageableHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  static String[] resourceLocations = {"classpath:/templates/", "classpath:/static/"};

  private final LoginUserArgumentResolver loginUserArgumentResolver;

  private final PageableHandler pageableHandler;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserArgumentResolver);
    resolvers.add(pageableHandler);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
            .addResourceLocations(resourceLocations);
  }

}
