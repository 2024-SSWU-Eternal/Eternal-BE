package com.example.eternal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
}
