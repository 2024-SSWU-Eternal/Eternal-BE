package com.example.eternal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public String sendVerificationEmail(String toEmail) throws MessagingException {
        // 6자리 랜덤 인증 번호 생성
        String verificationCode = generateVerificationCode();

        // 이메일 작성
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("이메일 인증 코드");
        helper.setText("인증 번호는 " + verificationCode + " 입니다.", true);

        // 이메일 전송
        mailSender.send(message);

        return verificationCode;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999 범위의 숫자 생성
        return String.valueOf(code);
    }
}
