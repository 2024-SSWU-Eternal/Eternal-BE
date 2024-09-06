package com.example.eternal.repository.test;

import com.example.eternal.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, String> {
}
