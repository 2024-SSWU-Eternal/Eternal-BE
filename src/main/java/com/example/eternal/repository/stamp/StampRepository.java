package com.example.eternal.repository.stamp;

import com.example.eternal.entity.Stamp;
import com.example.eternal.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Integer> {
    List<Stamp> findByUser(User user);
    Optional<Stamp> findByUserAndStampNum(User user, int stampNum);

    //리셋 코드
    @Modifying
    @Transactional
    @Query("UPDATE Stamp s SET s.stampSet=false ")
    void resetAllStampSet();
}
