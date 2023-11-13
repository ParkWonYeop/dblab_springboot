package com.example.spring_dblab.utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String ADMIN_ADDRESS;

    @Async
    public void sendMail(String sendTo, String word) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO,sendTo);
        message.setSubject("이벤트 알림");
        String text = word+" 단어를 포함한 이벤트가 등록되었습니다.";
        message.setText(text,"utf-8");
        message.setFrom(new InternetAddress(ADMIN_ADDRESS,"no-reply"));
        javaMailSender.send(message);
    }
}
