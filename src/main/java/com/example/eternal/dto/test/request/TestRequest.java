package com.example.eternal.dto.test.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestRequest {

    private Result result;

    @Getter
    @Setter
    public static class Result {
        private String type;
        private int score;
    }
}
