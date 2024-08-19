package com.example.eternal.service;

import com.example.eternal.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public String login(String username, String password) {
        return managerRepository.findByUsername(username)
                .map(manager -> {
                    if (manager.getPassword().equals(password)) {
                        return "로그인 성공!";
                    } else {
                        return "비밀번호가 일치하지 않습니다.";
                    }
                })
                .orElse("아이디가 존재하지 않습니다.");
    }
}
