package com.example.eternal.service.user;

import com.example.eternal.dto.user.request.LoginRequest;
import com.example.eternal.dto.user.request.RegisterRequest;
import com.example.eternal.entity.Stamp;
import com.example.eternal.repository.stamp.StampRepository;
import com.example.eternal.entity.User;
import com.example.eternal.repository.user.UserRepository;
import com.example.eternal.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StampRepository stampRepository;
    private final VerificationService verificationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, StampRepository stampRepository, VerificationService verificationService, JwtTokenProvider jwtTokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.stampRepository = stampRepository;
        this.verificationService = verificationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    // 로그인 로직
    public String authenticateUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 입력한 비밀번호와 저장된 비밀번호 비교
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // 비밀번호가 맞으면 JWT 토큰 생성 후 반환
                return jwtTokenProvider.generateToken(user.getEmail(), user.getStudentNumber());
            }
        }
        return null; // 인증 실패 시 null 반환
    }


    // 회원가입 로직 (인증된 이메일만 등록 가능)
    public Integer registerUser(RegisterRequest request) {

        // 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (!verificationService.isEmailVerified(request.getEmail())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        // 사용자 정보 설정 및 저장
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStudentNumber(request.getStudentNumber());
        user.setAllowed(request.isAllowed());

        User savedUser = userRepository.save(user);

        // 기본 스탬프 설정
        createDefaultStamp(savedUser);
        return savedUser.getStudentNumber();
    }

    // 기본 스탬프 설정
    private void createDefaultStamp(User user) {
        try {
            for (int i = 1; i <= 9; i++) {
                Stamp stamp = new Stamp();
                stamp.setStampNum(i);
                System.out.println("Stamp number being set: " + i);
                stamp.setImage("images/favicon.png");  // 동일한 이미지 사용
                stamp.setStampSet(false);  // 초기 상태는 false
                stamp.setUser(user);
                stampRepository.save(stamp);
            }
        } catch (Exception e) {
            System.err.println("스탬프 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // 이메일 인증 코드 전송
    public void sendVerificationEmail(String email) {
        String verificationCode = emailService.sendVerificationEmail(email);
        verificationService.saveVerificationCode(email, verificationCode); // 인증 코드 저장
    }

}
