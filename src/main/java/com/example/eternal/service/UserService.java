package com.example.eternal.service;

import com.example.eternal.dto.RegisterRequest;
import com.example.eternal.dto.LoginRequest;
import com.example.eternal.model.User;
import com.example.eternal.repository.UserRepository;
import com.example.eternal.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 로직
    public void registerUser(RegisterRequest request) {
        User user = new User();
        user.setStudentNumber(request.getStudentNumber()); // 학번 설정
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAllowed(request.isAllowed()); // 약관 동의 여부 설정
        userRepository.save(user);
    }

    // 로그인 로직
    public String authenticateUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtTokenProvider.generateToken(user.getEmail());
            }
        }
        return null; // 인증 실패 시 null 반환
    }
}
