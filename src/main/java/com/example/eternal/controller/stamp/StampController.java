package com.example.eternal.controller.stamp;

import com.example.eternal.dto.stamp.request.StampRequest;
import com.example.eternal.dto.stamp.response.StampResponse;
import com.example.eternal.entity.User;
import com.example.eternal.service.stamp.StampService;
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

    // 스탬프 획득 (QR 코드를 찍었을 때 호출되는 메서드)
    @PostMapping("/stamp/{stampId}")
    public ResponseEntity<?> acquireStamp(@PathVariable("stampId") int stampId, @AuthenticationPrincipal User user) {
        stampService.acquireStamp(stampId, user.getStudentNumber());
        return ResponseEntity.ok("스탬프가 성공적으로 등록되었습니다!");
    }

    // 스탬프 조회 (사용자의 스탬프 상태를 조회)
    @GetMapping("/stamp")
    public ResponseEntity<List<StampResponse>> getStamps(@AuthenticationPrincipal User user) {
        List<StampResponse> stamps = stampService.getStamps(user.getStudentNumber());
        return ResponseEntity.ok(stamps);
    }
}
