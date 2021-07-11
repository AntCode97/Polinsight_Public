package com.dns.polinsight.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  private final TemplateEngine templateEngine;

  /*
   * 일반 텍스트를 메일로 보냄
   * */
  @Async
  @Override
  public void sendMail(String to, String subject, String body) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setTo(to);  // 수신자
    helper.setSubject(subject); // 메일 제목
    helper.setText(body); // 메일 본문, html사용시 setText(html_content, true);

    mailSender.send(message);
  }

  /*
   * HTML로 된 데이터를 메일로 보냄
   * */
  @Async
  @Override
  public void sendTemplateMail(String to, String subject, Map<String, Object> variables) throws MessagingException {

    Context context = new Context();
    context.setVariables(variables);
    String htmlTemplate = templateEngine.process("mail/mail", context);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlTemplate, true);
    mailSender.send(message);
  }

}
