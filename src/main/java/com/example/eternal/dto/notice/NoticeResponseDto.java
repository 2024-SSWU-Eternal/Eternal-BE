package com.example.eternal.dto.notice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeResponseDto {
    private Long noticeId;
    private String title;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private List<String> images;
}
