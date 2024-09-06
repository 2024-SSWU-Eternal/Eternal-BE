package com.example.eternal.service.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class VerificationService {

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Set<String> verifiedEmails = new HashSet<>(); // 인증된 이메일을 저장할 Set

    // 이메일과 인증 코드를 저장
    public void saveVerificationCode(String email, String verificationCode) {
        verificationCodes.put(email, verificationCode);
    }

    // 입력된 인증 코드가 저장된 코드와 일치하는지 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // 이메일 인증 성공 시 호출되는 메서드
    public void markEmailAsVerified(String email) {
        verifiedEmails.add(email); // 인증된 이메일을 저장
    }

    // 이메일이 인증되었는지 확인하는 메서드
    public boolean isEmailVerified(String email) {
        return verifiedEmails.contains(email); // 인증된 이메일 목록에 포함되어 있는지 확인
    }

    // 인증 코드 삭제 (옵션)
    public void removeVerificationCode(String email) {
        verificationCodes.remove(email);
    }
}
