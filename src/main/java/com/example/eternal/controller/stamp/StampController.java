package com.example.eternal.controller.stamp;

import com.example.eternal.dto.stamp.request.StampRequest;
import com.example.eternal.dto.stamp.response.StampResponse;
import com.example.eternal.entity.User;
import com.example.eternal.security.JwtTokenProvider;
import com.example.eternal.service.stamp.StampService;
import com.example.eternal.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/user")
public class StampController {

    @Autowired
    private StampService stampService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    // 스탬프 획득 (QR 코드를 찍었을 때 호출되는 메서드)
    @PostMapping("/stamp/{stampNum}")
    public ResponseEntity<?> acquireStamp(@PathVariable("stampNum") int stampNum, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                Integer studentNumber = jwtTokenProvider.getStudentNumFromToken(token);
                System.out.println("토큰에서 추출된 학번: " + studentNumber); // 로그 추가

                stampService.acquireStamp(stampNum, studentNumber);
                StampResponse stampResponse = stampService.getStampByUserAndStampNum(studentNumber, stampNum);
                return ResponseEntity.ok("스탬프가 성공적으로 등록되었습니다!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/stamps")  // 액세스 레벨을 명확하게 설정
    public ResponseEntity<?> getStamps(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                Integer studentNumber = jwtTokenProvider.getStudentNumFromToken(token);
                List<StampResponse> stamps = stampService.getStamps(studentNumber);
                return ResponseEntity.ok(stamps);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        }  catch (Exception e) {
            log.error("Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
