package com.example.eternal.controller;

import com.example.eternal.dto.ManagerLoginRequest;
import com.example.eternal.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody ManagerLoginRequest managerLoginRequest) {
        String result = managerService.login(managerLoginRequest.getUsername(), managerLoginRequest.getPassword());

        if (result.equals("로그인 성공!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }
}