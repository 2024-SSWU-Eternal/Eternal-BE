package com.example.eternal.controller;

import com.example.eternal.dto.NoticeDto;
import com.example.eternal.entity.Notice;
import com.example.eternal.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNotice(
            @ModelAttribute NoticeDto noticeDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestParam String userId
    ) {
        noticeService.createNotice(noticeDto, imageFiles, userId);
        return ResponseEntity.ok("Notice created successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNotice(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("title", notice.getTitle());
        response.put("content", notice.getContent());
        response.put("createdAt", formatDate(notice.getCreatedAt()));

        // 이미지 파일 URL 리스트를 제공
        List<String> imageUrls = notice.getImages().stream()
                .map(image -> "/images/" + image.getSavedFilename())
                .collect(Collectors.toList());
        response.put("images", imageUrls);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();

        List<Map<String, Object>> response = notices.stream()
                .map(notice -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", notice.getTitle());
                    map.put("content", notice.getContent());
                    map.put("createdAt", formatDate(notice.getCreatedAt()));

                    // 이미지 파일 URL 리스트를 제공
                    List<String> imageUrls = notice.getImages().stream()
                            .map(image -> "/images/" + image.getSavedFilename())
                            .collect(Collectors.toList());
                    map.put("images", imageUrls);

                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
