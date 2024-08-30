package com.example.eternal.controller;

import com.example.eternal.dto.RegisterRequest;
import com.example.eternal.dto.LoginRequest;
import com.example.eternal.dto.ApiResponse;
import com.example.eternal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse("회원가입이 완료되었습니다."));
    }

    // 로그인
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        String token = userService.authenticateUser(request);
        if (token != null) {
            return ResponseEntity.ok(new ApiResponse("로그인이 완료되었습니다. JWT Token: " + token));
        } else {
            return ResponseEntity.status(401).body(new ApiResponse("로그인 실패: 잘못된 이메일 또는 비밀번호입니다."));
        }
    }
}
