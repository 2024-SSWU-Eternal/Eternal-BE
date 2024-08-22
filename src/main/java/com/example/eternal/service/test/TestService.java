package com.example.eternal.service.test;

import com.example.eternal.entity.TestResult;
import com.example.eternal.repository.test.TestResultRepository;
import com.example.eternal.dto.test.request.TestRequest;
import com.example.eternal.dto.test.response.TestResultResponse;
import com.example.eternal.dto.test.response.TestResultsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestResultRepository testResultRepository;

    @Transactional
    public void registerTestResult(TestRequest request) {
        TestResult result = testResultRepository.findById(request.getResult().getType())
                .orElse(new TestResult(request.getResult().getType(), 0));
//예외처리 구문 / 데베에 미리 8개 타입을 넣어야함
//        String type = request.getResult().getType();
//        TestResult result = testResultRepository.findById(type)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST, "Invalid test type: " + type));

        result.increaseCount();
        testResultRepository.save(result);
    }

    @Transactional(readOnly = true)
    public TestResultsResponse getTestResults() {
        List<TestResult> results = testResultRepository.findAll();
        int totalResponses = results.stream().mapToInt(TestResult::getCount).sum();

        List<TestResultResponse> resultResponses = results.stream()
                .map(result -> new TestResultResponse(
                        result.getType(),
                        result.getCount(),
                        calculatePercentage(result.getCount(), totalResponses)
                ))
                .collect(Collectors.toList());

        return new TestResultsResponse(totalResponses, resultResponses);
    }

    private int calculatePercentage(int count, int totalResponses) {
        if (totalResponses == 0) {
            return 0;
        }
        return (int) ((double) count / totalResponses * 100);
    }
}
