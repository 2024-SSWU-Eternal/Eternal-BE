package com.example.eternal.service.stamp;

import com.example.eternal.dto.stamp.response.StampResponse;
import com.example.eternal.entity.Stamp;
import com.example.eternal.entity.User;
import com.example.eternal.repository.stamp.StampRepository;
import com.example.eternal.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StampService {

    @Autowired
    private StampRepository stampRepository;

    @Autowired
    private UserRepository userRepository;

    public void acquireStamp(int stampNum, Integer studentNumber) {
        // studentNumber로 사용자 조회
        User user = userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Stamp stamp = stampRepository.findByUserAndStampNum(user, stampNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 스탬프를 찾을 수 없습니다."));

        // stampSet 값을 반드시 설정해야 함
        stamp.setStampSet(true); // 또는 false, 상황에 맞게 설정
        stamp.setStampNum(stampNum);

        stamp.setUser(user); // User를 설정하면서 studentNumber도 자동으로 설정
       // stamp.setImage("default_image.png");
        stampRepository.save(stamp);
    }


    // 사용자의 스탬프 상태 조회
    public List<StampResponse> getStamps(Integer studentNumber) {
        User user = userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        List<Stamp> stamps = stampRepository.findByUser(user);

        return stamps.stream()
                .map(stamp -> new StampResponse(stamp.getStampNum(), stamp.getImage(), stamp.getStampSet(), user.getName()))
                .collect(Collectors.toList());
    }

    //stamp reset
    @Transactional
    @Scheduled(cron = "0 0 15 * * ?")
    public void resetAllStampSet() {
        stampRepository.resetAllStampSet();
        System.out.println("모든 사용자의 스탬프를 리셋함.");
    }

    //stamp image return
    public StampResponse getStampByUserAndStampNum(Integer studentNumber, int stampNum) {
        User user = userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Stamp stamp = stampRepository.findByUserAndStampNum(user, stampNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 스탬프를 찾을 수 없습니다."));

        return new StampResponse(stamp.getStampNum(), stamp.getImage(), stamp.getStampSet(), user.getName());
    }

}


