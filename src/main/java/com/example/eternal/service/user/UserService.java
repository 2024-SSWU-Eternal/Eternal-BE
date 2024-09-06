package com.example.eternal.service.user;

import com.example.eternal.dto.user.request.LoginRequest;
import com.example.eternal.dto.user.request.RegisterRequest;
import com.example.eternal.entity.Stamp;
import com.example.eternal.repository.stamp.StampRepository;
import com.example.eternal.entity.User;
import com.example.eternal.repository.user.UserRepository;
import com.example.eternal.security.JwtTokenProvider;
import com.example.eternal.service.user.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // PasswordEncoder로 변경
    private final StampRepository stampRepository;
    private final VerificationService verificationService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, StampRepository stampRepository, VerificationService verificationService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;  // PasswordEncoder 사용
        this.stampRepository = stampRepository;
        this.verificationService = verificationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 로그인 로직
    public String authenticateUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 입력한 비밀번호와 저장된 비밀번호 비교
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // 비밀번호가 맞으면 JWT 토큰 생성 후 반환
                return jwtTokenProvider.generateToken(user.getEmail());
            }
        }
        return null; // 인증 실패 시 null 반환
    }

    // 회원가입 로직
    public Integer registerUser(RegisterRequest request) {
//        // 이메일 인증 확인
//        if (!verificationService.verifyCode(request.getEmail(), request.getVerificationCode())) {
//            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
//        }

        // 새 유저 생성
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        // 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStudentNumber(request.getStudentNumber());
        user.setAllowed(request.isAllowed()); // 약관 동의 여부 설정

        // 유저 저장
        User savedUser = userRepository.save(user);

        // 기본 스탬프 설정
        createDefaultStamp(savedUser);
        return savedUser.getStudentNumber(); // 유저 학번 반환
    }

    // 기본 스탬프 설정
    private void createDefaultStamp(User user) {
        for (int i = 1; i <= 9; i++) {
            Stamp stamp = new Stamp();
            stamp.setStampNum(i);
            System.out.println("Stamp number being set: " + i);
            stamp.setImage("images/favicon.png");  // 동일한 이미지 사용
            stamp.setStampSet(false);  // 초기 상태는 false
            stamp.setUser(user);
            stampRepository.save(stamp);
        }
    }
}
