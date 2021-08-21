package com.dns.polinsight.config.filter;

import com.dns.polinsight.types.Address;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
//@Component
public class CustomTypeParameterArgsResolver implements HandlerMethodArgumentResolver {


  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(Email.class) ||
        parameter.getParameterType().equals(Phone.class) ||
        parameter.getParameterType().equals(Address.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    if (parameter.getParameterType().equals(Email.class)) {
      //      webRequest.getAttributeNames().
    } else if (parameter.getParameterType().equals(Phone.class)) {
    } else if (parameter.getParameterType().equals(Address.class)) {
    }
    return null;
  }

}
