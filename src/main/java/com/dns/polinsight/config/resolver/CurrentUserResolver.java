package com.dns.polinsight.config.resolver;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

  private final UserService userService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean isCurrentUser = parameter.getParameterAnnotation(CurrentUser.class) != null;
    boolean isUser = parameter.getParameterType().equals(User.class);
    return isCurrentUser && isUser;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String principal = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    if (principal.equals("anonymousUser")) {
      return null;
    }
    return userService.loadUserByUsername(principal);
  }

}
