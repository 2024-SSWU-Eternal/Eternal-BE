package com.example.eternal.entity.manager;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "image")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "saved_filename", length = 255)
    private String savedFilename;
}
