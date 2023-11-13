package com.example.spring_dblab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Spring Mail 서비스를 위한 구성 클래스.
 * 메일 서버의 호스트, 포트, 사용자 이름, 비밀번호 등을 설정하고 JavaMailSender를 구성한다.
 */
@Configuration
public class MailConfig {
    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    /**
     * JavaMailSender 빈을 구성하고 반환합니다.
     *
     * @return 구성된 JavaMailSender
     */
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        javaMailSender.setPort(port);

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    /**
     * 메일 서버의 속성을 설정합니다.
     * SMTP 프로토콜, 인증, TLS, 디버그 모드 등의 속성을 포함합니다.
     *
     * @return 메일 서버의 속성을 담은 Properties 객체
     */
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.starttls.required","true");
        return properties;
    }
}
