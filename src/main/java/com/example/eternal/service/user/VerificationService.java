package com.example.eternal.service.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {

    private final Map<String, String> verificationCodes = new HashMap<>();

    // 이메일과 인증 코드를 저장
    public void saveVerificationCode(String email, String verificationCode) {
        verificationCodes.put(email, verificationCode);
    }

    // 입력된 인증 코드가 저장된 코드와 일치하는지 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // 인증 코드 삭제 (옵션)
    public void removeVerificationCode(String email) {
        verificationCodes.remove(email);
    }
}
