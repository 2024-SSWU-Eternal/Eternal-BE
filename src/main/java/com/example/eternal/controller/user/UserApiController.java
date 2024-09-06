package com.example.eternal.controller.user;

import com.example.eternal.dto.user.response.ApiResponse;
import com.example.eternal.dto.user.request.LoginRequest;
import com.example.eternal.dto.user.request.RegisterRequest;
import com.example.eternal.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = userService.authenticateUser(request);
        if (token != null) {
            // JWT 토큰을 쿠키에 설정
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);          // JavaScript에서 접근 금지
            cookie.setSecure(true);            // HTTPS에서만 전송
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);    // 1일 동안 유효
            cookie.setAttribute("SameSite", "Strict"); // CSRF 보호 강화

            response.addCookie(cookie);

            return ResponseEntity.ok(new ApiResponse("로그인이 완료되었습니다.", token));
        } else {
            return ResponseEntity.status(401).body(new ApiResponse("로그인 실패: 잘못된 이메일 또는 비밀번호입니다."));
        }
    }

    // 로그아웃
    @PostMapping("/user/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        // 쿠키에서 JWT 토큰 삭제
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 만료
        cookie.setAttribute("SameSite", "Strict");

        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiResponse("로그아웃이 완료되었습니다."));
    }
}
