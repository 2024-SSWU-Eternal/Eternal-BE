package com.example.eternal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private int studentNumber;     // 학번 (정수형)
    private String name;           // 이름
    private String email;          // 이메일
    private String password;       // 비밀번호
    private boolean allowed;       // 약관 동의 여부
}

@Getter
@Setter
public class LoginRequest {
    private String email;          // 이메일
    private String password;       // 비밀번호
}

@Getter
@Setter
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }
}
