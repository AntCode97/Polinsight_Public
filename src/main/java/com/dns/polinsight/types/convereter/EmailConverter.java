package com.dns.polinsight.types.convereter;

import com.dns.polinsight.types.Email;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class EmailConverter {

  @Component
  public static class StringToEmailConverter implements Converter<String, Email> {

    @Override
    public Email convert(String source) {
      String[] emails = source.split("@");
      return Email.builder()
                  .account(emails[0])
                  .domain(emails[1])
                  .build();
    }

  }

  @Component
  public static class EmailToStringConverter implements Converter<Email, String> {

    @Override
    public String convert(Email email) {
      return email.getAccount() + "@" + email.getDomain();
    }

  }

}