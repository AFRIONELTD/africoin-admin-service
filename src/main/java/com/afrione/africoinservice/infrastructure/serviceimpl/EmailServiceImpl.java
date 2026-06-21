package com.afrione.africoinservice.infrastructure.serviceimpl;

import com.afrione.africoinservice.domain.models.email.EmailRequest;
import com.afrione.africoinservice.domain.services.EmailService;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final String defaultSender;
  private final String domain;
  private final MailgunMessagesApi mailgunMessagesApi;

  public EmailServiceImpl(
      @Value("${MAILGUN_API_KEY}") String key,
      @Value("${mailgun.default-sender:info@afrione.co}") String defaultSender,
      @Value("${mailgun.domain:noreply.afrione.co}") String domain) {
    this.defaultSender = defaultSender;
    this.domain = domain;
    this.mailgunMessagesApi = MailgunClient.config(key).createApi(MailgunMessagesApi.class);
  }

  private TemplateEngine templateEngine;

  @Autowired
  public void setTemplateEngine(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  @Async
  public void send(EmailRequest emailRequest) {

    String emailBody;
    if (emailRequest.getVariables() != null) {
      Context context = new Context();
      emailRequest.getVariables().forEach(context::setVariable);
      emailBody = templateEngine.process(emailRequest.getTemplateName(), context);
    } else {
      emailBody = emailRequest.getBody();
    }
    emailRequest.setBody(emailBody);

    Message message =
        Message.builder()
            .from(defaultSender)
            .to(emailRequest.getRecipient())
            .subject(emailRequest.getSubject())
            .html(emailBody)
            .build();
    MessageResponse messageResponse = mailgunMessagesApi.sendMessage(domain, message);
    System.out.println(messageResponse.toString());
  }

  @Async
  @Override
  public void sendNotice(
      TemplateType detail, String recipient, Map<String, Object> variables) {
    log.info("Sending notice to {} - {}", detail.getSubject(), detail);
    EmailRequest emailRequest = new EmailRequest();
    emailRequest.setSubject(detail.getSubject());

    emailRequest.setTemplateName(detail.getTemplateFileName());

    emailRequest.setRecipient(recipient);
    emailRequest.setVariables(variables);
    send(emailRequest);
  }

  @Async
  @Override
  public void sendNotice(
      TemplateType detail, Set<String> recipients, Map<String, Object> variables) {
    log.info("Sending notice to {} - {}", detail.getSubject(), detail);

    for (String recipient : recipients) {
      EmailRequest emailRequest = new EmailRequest();
      emailRequest.setSubject(detail.getSubject());

      emailRequest.setTemplateName(detail.getTemplateFileName());

      emailRequest.setRecipient(recipient);
      emailRequest.setVariables(variables);
      send(emailRequest);
    }
  }

  @Async
  public void sendNotice(String recipient, String body, String tile){

      EmailRequest emailRequest = new EmailRequest();
      emailRequest.setBody(body);
      emailRequest.setRecipient(recipient);
      emailRequest.setSubject(tile);

      send(emailRequest);


  }
}
