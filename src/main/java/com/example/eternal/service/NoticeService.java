package com.example.eternal.service;

import com.example.eternal.dto.NoticeDto;
import com.example.eternal.entity.Image;
import com.example.eternal.entity.Notice;
import com.example.eternal.entity.Manager;
import com.example.eternal.repository.ImageRepository;
import com.example.eternal.repository.ManagerRepository;
import com.example.eternal.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
    public void createNotice(NoticeDto noticeDto, List<MultipartFile> imageFiles, String userId) {
        Manager manager = managerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }
}


