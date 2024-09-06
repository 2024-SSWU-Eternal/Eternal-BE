package com.example.eternal.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "Currently logged in user: " + userDetails.getUsername();
    }
}