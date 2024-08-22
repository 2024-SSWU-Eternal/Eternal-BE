package com.example.eternal.controller.test;

import com.example.eternal.dto.test.request.TestRequest;
import com.example.eternal.dto.test.response.TestResultsResponse;
import com.example.eternal.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/test")
    public ResponseEntity<String> registerTestResult(@RequestBody TestRequest request) {
        testService.registerTestResult(request);
        return ResponseEntity.ok("스탬프 등록 완료");
    }

    @GetMapping("/test")
    public TestResultsResponse getTestResults() {
        return testService.getTestResults();
    }
}
