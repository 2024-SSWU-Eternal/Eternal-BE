package com.example.eternal.repository.manager;

import com.example.eternal.entity.manager.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 필요에 따라 커스텀 쿼리 메서드를 추가할 수 있습니다.
}