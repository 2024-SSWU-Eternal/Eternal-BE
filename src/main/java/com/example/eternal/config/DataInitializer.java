package com.example.eternal.config;

import com.example.eternal.entity.Stamp;
import com.example.eternal.repository.stamp.StampRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final StampRepository stampRepository;

    @PostConstruct
    public void init() {
        if(stampRepository.count() > 0) { return; } //stamp가 이미 9개 존재하면 초기화 ㄴ

        //stamp 생성
        for(int i =1; i <= 9; i++) {
            Stamp stamp = new Stamp();
            stamp.setStampNum(i);
            stamp.setImage("images/favicon.png"); //stamp별로 이미지 설정할 때는 걍 +i
            stamp.setStampSet(false); //stamp초기는 획득 ㄴㄴ
            stampRepository.save(stamp);
        }
    }

}
