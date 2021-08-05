package com.dns.polinsight.utils;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class PageableHandler implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean isPageHandler = parameter.getParameterAnnotation(PageHandler.class) != null;
    boolean isPageable = parameter.getParameterType().equals(Pageable.class);
    return isPageHandler && isPageable;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    int size = webRequest.getParameterMap().containsKey("size") ? Integer.parseInt(webRequest.getParameter("size")) : 10;
    int offset = webRequest.getParameterMap().containsKey("offset") ? Integer.parseInt(webRequest.getParameter("offset")) : 0;
    return PageRequest.of(offset, size);
  }

}
