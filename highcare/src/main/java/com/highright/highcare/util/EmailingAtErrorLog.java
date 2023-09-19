package com.highright.highcare.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailingAtErrorLog extends AppenderBase<ILoggingEvent> {

    private String toAddress;
    private String fromAddress;
    private String smtpHost;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            sendEmail(event.getFormattedMessage());
        }
    }

    private void sendEmail(String message) {
        // JavaMail API를 사용하여 이메일 보내기 로직 작성
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(fromAddress));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            emailMessage.setSubject("Error Log");
            emailMessage.setText(message);

            Transport.send(emailMessage);
        } catch (MessagingException e) {
            // 이메일 보내기 중 오류 발생 시 처리
            e.printStackTrace();
        }
    }

    // Getter 및 Setter 메서드
}
