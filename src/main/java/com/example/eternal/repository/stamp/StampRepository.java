package com.example.eternal.repository.stamp;

import com.example.eternal.entity.Stamp;
import com.example.eternal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Integer> {
    List<Stamp> findByUser(User user);
    Optional<Stamp> findByUserAndStampNum(User user, int stampNum);
}
