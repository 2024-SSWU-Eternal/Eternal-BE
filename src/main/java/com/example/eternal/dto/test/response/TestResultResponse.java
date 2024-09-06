package com.example.eternal.dto.test.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestResultResponse {
    private final String type;
    private final int count;
    private final int percentage;
}
