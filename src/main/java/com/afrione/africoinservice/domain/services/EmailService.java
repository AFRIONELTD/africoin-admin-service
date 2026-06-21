package com.afrione.africoinservice.domain.services;


import java.util.Map;
import java.util.Set;

public interface EmailService {

    enum TemplateType {
        EMAIL_OTP("otp-verification.html", "Your Africoin One Time Password (OTP)"),
        EMAIL_OTC_B2C("afr-btc-otp-verification.html", "Your Africoin One Time Password (OTP)"),
        WELCOME("welcome.html", "Welcome to Africoin"),
        SUPER_ADMIN_ODER_APPROVAL(
                "afrione-super-admin_approval-email.html",
                "Africoin Super Admin - Order is Ready for Approval"),
        ADMIN_ORDER_CONFIRMATION("afrione-admin-order-confirmation-email.html", "Order Confirmed"),
        ADMIN_AUTO_CONFIRMATION("afrione-auto-confirmation-admin-email.html", "Order Auto Confirmed"),
        ADMIN_ORDER_APPROVAL(
                "afrione-admin-notification-on-order-approval.html", "Admin Order Approval"),
        SUPER_ADMIN_ORDER_APPROVAL_OTP_CODE(
                "afrione-super-admin-otp.html", "Your Africoin Super Admin Order Approval OTP Code"),
        SUPER_ADMIN_PAYMENT_APPROVED(
                "afrione-super-admin-payment-confirm.html",
                "Africoin Super Admin Payment Approved Notification"),
        ADMIN_PAYMENT_APPROVED(
                "afrione-admin-payment-confirm.html", "Africoin Admin Payment Approved Notification"),
        AMIN_FORGOT_PASSWORD(
                "admin-forgot-password.html", "Africoin Admin Password Reset Notification"),
        ;
        private final String templateFileName;
        private final String subject;

        TemplateType(String templateName, String subject) {
            this.templateFileName = templateName;
            this.subject = subject;
        }

        public String getTemplateFileName() {
            return templateFileName;
        }

        public String getSubject() {
            return subject;
        }
    }

    //void send(EmailRequest emailRequest);

    void sendNotice(EmailService.TemplateType detail, String recipient, Map<String, Object> models);

    void sendNotice(
            EmailService.TemplateType detail, Set<String> recipient, Map<String, Object> models);

    void sendNotice(String recipient, String body, String tile);
}
