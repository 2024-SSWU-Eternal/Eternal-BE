package com.example.eternal.dto.test.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TestResultsResponse {
    private final int totalResponses;
    private final List<TestResultResponse> results;
}
