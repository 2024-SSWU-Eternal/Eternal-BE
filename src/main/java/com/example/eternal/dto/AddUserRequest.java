package com.example.eternal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private int studentNumber;
    private String name;
    private String email;
    private String password;
}