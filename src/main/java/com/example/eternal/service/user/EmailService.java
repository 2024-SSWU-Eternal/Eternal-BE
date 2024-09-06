package com.example.eternal.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${mailgun.api.key}")
    private String mailgunApiKey;

    @Value("${mailgun.domain}")
    private String mailgunDomain;

    @Value("${mailgun.from}")
    private String fromEmail;

    // 이메일로 인증 코드 전송
    public String sendVerificationEmail(String toEmail) {
        String verificationCode = generateVerificationCode();

        try {
            HttpResponse<String> response = Unirest.post("https://api.mailgun.net/v3/" + mailgunDomain + "/messages")
                    .basicAuth("api", mailgunApiKey)
                    .queryString("from", fromEmail)
                    .queryString("to", toEmail)
                    .queryString("subject", "이메일 인증 코드")
                    .queryString("text", "인증 코드는 " + verificationCode + " 입니다.")
                    .asString();

            if (response.getStatus() == 200) {
                return verificationCode;
            } else {
                throw new IllegalStateException("이메일 전송 실패: " + response.getBody());
            }
        } catch (UnirestException e) {
            throw new IllegalStateException("이메일 전송 중 오류 발생", e);
        }
    }

    // 6자리 랜덤 인증 코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // 100000 ~ 999999 범위의 숫자 생성
    }
}