package com.example.eternal.controller.stamp;

import com.example.eternal.dto.stamp.request.StampRequest;
import com.example.eternal.dto.stamp.response.StampResponse;
import com.example.eternal.entity.User;
import com.example.eternal.security.JwtTokenProvider;
import com.example.eternal.service.stamp.StampService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class StampController {

    @Autowired
    private StampService stampService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 스탬프 획득 (QR 코드를 찍었을 때 호출되는 메서드)
    @PostMapping("/stamp/{stampNum}")
    public ResponseEntity<?> acquireStamp(@PathVariable("stampNum") int stampNum, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Integer studentNumber = jwtTokenProvider.getStudentNumFromToken(token);
        stampService.acquireStamp(stampNum, studentNumber);
        return ResponseEntity.ok("스탬프가 성공적으로 등록되었습니다!");
    }

    // 스탬프 조회 (사용자의 스탬프 상태를 조회)
    public ResponseEntity<?> getStamps(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Integer studentNumber = jwtTokenProvider.getStudentNumFromToken(token);
        return ResponseEntity.ok(stampService.getStamps(studentNumber));
    }

}
