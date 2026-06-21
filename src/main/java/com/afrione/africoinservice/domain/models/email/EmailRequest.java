package com.afrione.africoinservice.domain.models.email;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class EmailRequest {
  String recipient;
  String subject;
  String body;
  String templateName;
  Map<String, Object> variables;

  public EmailRequest(String recipient, String subject, String body) {
    this.recipient = recipient;
    this.subject = subject;
    this.body = body;
  }
}
