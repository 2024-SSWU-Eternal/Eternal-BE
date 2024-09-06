package com.example.eternal.dto.user.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private String message;
    private String token; // 토큰 필드 추가

    // 생성자 오버로드: 토큰이 없을 때
    public ApiResponse(String message) {
        this.message = message;
    }

    // 생성자 오버로드: 토큰이 있을 때
    public ApiResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getter와 Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
