package com.example.eternal.service.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {

    private final Map<String, String> verificationStorage = new HashMap<>();

    public void saveVerificationCode(String email, String verificationCode) {
        verificationStorage.put(email, verificationCode);
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationStorage.get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    public void removeVerificationCode(String email) {
        verificationStorage.remove(email);
    }
}
