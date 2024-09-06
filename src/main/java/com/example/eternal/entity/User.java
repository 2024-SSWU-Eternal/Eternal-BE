package com.example.eternal.entity;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int studentNumber;     // 학번 (정수형)
    private String name;           // 이름
    private String email;          // 이메일
    private String password;       // 비밀번호
    private boolean allowed;       // 약관 동의 여부
    private String verificationCode; // 인증번호
}