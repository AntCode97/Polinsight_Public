package com.dns.polinsight.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final HttpSession session;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
    boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
    return isLoginUserAnnotation && isUserClass;
  }

  /*
   * supportsParameter의 반환값이 true이면 세션에서 user를 가져와서 반환
   * */
  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    return session.getAttribute("user");
  }

}