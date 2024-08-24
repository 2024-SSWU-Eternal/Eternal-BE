package com.example.eternal.repository.manager;

import com.example.eternal.entity.manager.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
