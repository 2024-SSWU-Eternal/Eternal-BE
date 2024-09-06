package com.example.eternal.service.stamp;

import com.example.eternal.dto.stamp.request.StampRequest;
import com.example.eternal.dto.stamp.response.StampResponse;
import com.example.eternal.entity.Stamp;
import com.example.eternal.entity.User;
import com.example.eternal.repository.stamp.StampRepository;
import com.example.eternal.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 특정 스탬프 획득 (QR 코드에 따라 호출됨)
    public void acquireStamp(int stampId, Integer studentNumber) {
        User user = userRepository.findByStudentNumber(studentNumber).orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Stamp stamp = stampRepository.findById(stampId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스탬프를 찾을 수 없습니다."));

        // 스탬프가 해당 사용자에게 속해 있는지 확인
        if (!stamp.getUser().equals(user)) {
            throw new IllegalArgumentException("해당 스탬프는 이 사용자에게 할당되지 않았습니다.");
        }

        stamp.setStampSet(true); // 스탬프의 상태를 true로 변경
        stampRepository.save(stamp);
    }

    // 사용자의 스탬프 상태 조회
    public List<StampResponse> getStamps(Integer studentNumber) {
        User user = userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        List<Stamp> stamps = stampRepository.findByUser(user);

        return stamps.stream()
                .map(stamp -> new StampResponse(stamp.getStampNum(), stamp.getImage(), stamp.getStampSet()))
                .collect(Collectors.toList());
    }
}
