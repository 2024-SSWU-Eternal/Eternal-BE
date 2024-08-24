package com.example.eternal.entity.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
    private String username;

    @Column(name = "password", length = 20)
    private String password;

    // 기본 생성자
    public Manager() {}

    // 생성자
    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // username getter
    public String getUsername() {
        return username;
    }

    // username setter
    public void setUsername(String username) {
        this.username = username;
    }

    // password getter
    public String getPassword() {
        return password;
    }

    // password setter
    public void setPassword(String password) {
        this.password = password;
    }
}
