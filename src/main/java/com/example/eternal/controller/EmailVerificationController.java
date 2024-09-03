package com.example.eternal.controller;

import com.example.eternal.dto.ApiResponse;
import com.example.eternal.service.EmailService;
import com.example.eternal.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailService emailService;
    private final VerificationService verificationService;

    // 이메일 인증 코드 전송
    @PostMapping("/user/send-verification-code")
    public ResponseEntity<ApiResponse> sendVerificationCode(@RequestParam String email) {
        try {
            String verificationCode = emailService.sendVerificationEmail(email);
            verificationService.saveVerificationCode(email, verificationCode);
            return ResponseEntity.ok(new ApiResponse("인증 코드가 전송되었습니다."));
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(new ApiResponse("이메일 전송에 실패했습니다."));
        }
    }

    // 이메일 인증 코드 검증
    @PostMapping("/user/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = verificationService.verifyCode(email, code);
        if (isVerified) {
            verificationService.removeVerificationCode(email); // 인증 후 코드 삭제
            return ResponseEntity.ok(new ApiResponse("이메일 인증이 완료되었습니다."));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("잘못된 인증 코드입니다."));
        }
    }
}
