package com.example.eternal.service.notice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private AmazonS3 amazonS3; // S3 클라이언트

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
    }
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }
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

        noticeRepository.save(notice);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                Image image = uploadImageToS3(file, notice); // S3에 이미지 업로드
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
                imageRepository.delete(image);  // 이미지 엔티티 삭제
                deleteImageFromS3(image.getSavedFilename()); // S3에서 이미지 삭제
            }
            notice.getImages().clear();  // Notice에서 이미지 리스트도 비움
        }

        // 새로운 이미지 추가 로직
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                Image image = uploadImageToS3(file, notice); // S3에 이미지 업로드
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
                deleteImageFromS3(image.getSavedFilename()); // S3에서 이미지 삭제
            }
        }

        // 공지사항 삭제
        noticeRepository.delete(notice);
    }

    // S3에 이미지 업로드
    private Image uploadImageToS3(MultipartFile imageFile, Notice notice) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;

            // S3에 파일 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(imageFile.getContentType()); // Content-Type 설정

            amazonS3.putObject(new PutObjectRequest(bucketName, newFilename, imageFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            Image image = new Image();
            image.setOriginalFilename(originalFilename);
            image.setSavedFilename(newFilename);  // 암호화된 파일명을 savedFilename에 저장
            image.setNotice(notice);

            return image;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    // S3에서 파일을 조회할 때 사용할 URL 생성
    public String generateS3ImageUrl(String savedFilename) {
        return amazonS3.getUrl(bucketName, savedFilename).toString();
    }
    // S3에서 이미지 삭제
    private void deleteImageFromS3(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }
}
