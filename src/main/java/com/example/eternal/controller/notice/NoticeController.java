package com.example.eternal.controller.notice;

import com.example.eternal.dto.notice.NoticeRequestDto;
import com.example.eternal.dto.notice.NoticeResponseDto;
import com.example.eternal.entity.Notice;
import com.example.eternal.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<NoticeResponseDto> createNotice(
            @ModelAttribute NoticeRequestDto noticeDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestParam String userId
    ) {
        Notice notice = noticeService.createNotice(noticeDto, imageFiles, userId);
        NoticeResponseDto response = convertToResponseDto(notice);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long id,
            @ModelAttribute NoticeRequestDto noticeDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles
    ) {
        Notice notice = noticeService.updateNotice(id, noticeDto, imageFiles);
        NoticeResponseDto response = convertToResponseDto(notice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);
        NoticeResponseDto response = convertToResponseDto(notice);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        List<NoticeResponseDto> response = notices.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok("Notice deleted successfully!");
    }

    private NoticeResponseDto convertToResponseDto(Notice notice) {
        NoticeResponseDto dto = new NoticeResponseDto();
        dto.setNoticeId(notice.getNoticeId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setCreatedAt(formatDate(notice.getCreatedAt()));
        dto.setModifiedAt(formatDate(notice.getModifiedAt()));
        dto.setImages(notice.getImages().stream()
                .map(image -> noticeService.generateS3ImageUrl(image.getSavedFilename()))  // S3 URL 생성
                .collect(Collectors.toList()));
        return dto;
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
