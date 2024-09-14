package com.example.eternal.controller.user;

import com.example.eternal.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getUserName(@AuthenticationPrincipal UserDetails userDetails) {
        // 현재 로그인된 사용자의 이메일로 이름을 조회
        return userService.getUserNameByEmail(userDetails.getUsername());
    }
}
