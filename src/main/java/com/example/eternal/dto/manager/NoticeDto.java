package com.example.eternal.dto.manager;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NoticeDto {
    private String title;
    private String content;
    private String createdAt;
}
