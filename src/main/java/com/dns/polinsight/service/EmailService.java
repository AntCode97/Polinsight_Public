package com.dns.polinsight.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {

  void sendMail(String to, String subject, String body) throws MessagingException;

  void sendTemplateMail(String to, String subject, Map<String, Object> variables) throws MessagingException;

}
