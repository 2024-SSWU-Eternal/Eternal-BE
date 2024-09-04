package com.example.eternal.service.user;

import com.example.eternal.dto.user.request.RegisterRequest;
import com.example.eternal.entity.Stamp;
import com.example.eternal.repository.stamp.StampRepository;
import com.example.eternal.entity.User;
import com.example.eternal.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final StampRepository stampRepository;

    public Integer save(RegisterRequest request){
        //user 정보 저장
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setStudentNumber(request.getStudentNumber());
        user.setAllowed(request.isAllowed());

        User savedUser = userRepository.save(user);

        createDefaultStamp(savedUser); //stamp랑 user연결
        return savedUser.getStudentNumber();//user학번 반환
    }

    //stamp : user 생성 시 함께 사용자에게 할당
    private  void createDefaultStamp(User user){
        for (int i = 1; i <= 9; i++) {
            Stamp stamp = new Stamp();
            stamp.setStampNum(i);
            stamp.setImage("images/favicon.png"); // 동일한 이미지 사용
            stamp.setStampSet(false); // 초기 상태는 false
            stamp.setUser(user);
            stampRepository.save(stamp);
        }
    }

}
