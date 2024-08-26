package com.example.eternal.service.notice;

import com.example.eternal.dto.notice.NoticeRequestDto;
import com.example.eternal.entity.Image;
import com.example.eternal.entity.Notice;
import com.example.eternal.entity.Manager;
import com.example.eternal.example.InvalidRequestException;
import com.example.eternal.example.ResourceNotFoundException;
import com.example.eternal.repository.manager.ImageRepository;
import com.example.eternal.repository.manager.ManagerRepository;
import com.example.eternal.repository.manager.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Notice createNotice(NoticeRequestDto noticeDto, List<MultipartFile> imageFiles, String userId) {
        if (noticeDto == null || noticeDto.getTitle() == null || noticeDto.getTitle().isEmpty() ||
                noticeDto.getContent() == null || noticeDto.getContent().isEmpty()) {
            throw new InvalidRequestException("Title and content must not be empty");
        }

        if (userId == null || userId.isEmpty()) {
            throw new InvalidRequestException("User ID must not be null or empty");
        }

        Manager manager = managerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setCreatedAt(LocalDateTime.now());
        notice.setModifiedAt(LocalDateTime.now());
        notice.setManager(manager);

        noticeRepository.save(notice);  // Notice 저장

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                Image image = saveImage(file, notice);
                if (image != null) {
                    imageRepository.save(image);
                }
            }
        }

        return notice;
    }

    @Transactional
    public Notice updateNotice(Long id, NoticeRequestDto noticeDto, List<MultipartFile> imageFiles) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setModifiedAt(LocalDateTime.now());

        // 기존 이미지 삭제 로직
        if (!notice.getImages().isEmpty()) {
            for (Image image : notice.getImages()) {
                imageRepository.delete(image);  // 기존 이미지 삭제
                // 실제 파일도 삭제하려면 아래 코드 추가
                Path filePath = Paths.get(System.getProperty("user.dir") + "/uploaded-images/", image.getSavedFilename());
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            notice.getImages().clear();  // Notice에서 이미지 리스트도 비움
        }

        // 새로운 이미지 추가 로직
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                Image image = saveImage(file, notice);
                if (image != null) {
                    imageRepository.save(image);  // 새로운 이미지 저장
                }
            }
        }

        return noticeRepository.save(notice);  // 수정된 내용을 저장하고 반환
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        // 연관된 이미지 삭제
        if (!notice.getImages().isEmpty()) {
            for (Image image : notice.getImages()) {
                imageRepository.delete(image);  // 이미지 엔티티 삭제
                // 실제 파일도 삭제하려면 아래 코드 추가
                Path filePath = Paths.get(System.getProperty("user.dir") + "/uploaded-images/", image.getSavedFilename());
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 공지사항 삭제
        noticeRepository.delete(notice);
    }

    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    private Image saveImage(MultipartFile imageFile, Notice notice) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String currentDir = System.getProperty("user.dir");
            String uploadDir = currentDir + "/uploaded-images/";
            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFilename);
            imageFile.transferTo(filePath.toFile());

            Image image = new Image();
            image.setOriginalFilename(originalFilename);
            image.setSavedFilename(newFilename);
            image.setNotice(notice);

            return image;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
