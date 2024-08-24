package com.example.eternal.controller.manager;

import com.example.eternal.dto.manager.NoticeDto;
import com.example.eternal.entity.manager.Notice;
import com.example.eternal.service.manager.NoticeService;
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
        response.put("notice_id", notice.getNoticeId());
        response.put("title", notice.getTitle());
        response.put("content", notice.getContent());
        response.put("createdAt", formatDate(notice.getCreatedAt()));
        response.put("modifiedAt", formatDate(notice.getModifiedAt()));  // 수정된 날짜 출력

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
                    map.put("notice_id", notice.getNoticeId());
                    map.put("title", notice.getTitle());
                    map.put("content", notice.getContent());
                    map.put("createdAt", formatDate(notice.getCreatedAt()));
                    map.put("modifiedAt", formatDate(notice.getModifiedAt()));  // 수정된 날짜 출력

                    List<String> imageUrls = notice.getImages().stream()
                            .map(image -> "/images/" + image.getSavedFilename())
                            .collect(Collectors.toList());
                    map.put("images", imageUrls);

                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateNotice(
            @PathVariable Long id,
            @ModelAttribute NoticeDto noticeDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles
    ) {
        noticeService.updateNotice(id, noticeDto, imageFiles);
        return ResponseEntity.ok("Notice updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok("Notice deleted successfully!");
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
