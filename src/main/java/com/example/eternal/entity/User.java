package com.example.eternal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int studentNumber;     // 학번 (정수형)
    private String name;           // 이름
    private String email;          // 이메일
    private String password;       // 비밀번호
    private boolean allowed;       // 약관 동의 여부
    private boolean emailVerified; // 이메일 인증 여부 (true/false)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stamp> stamps;

}