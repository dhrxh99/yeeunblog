package com.yeeun.yeeunblog.service;

import com.yeeun.yeeunblog.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp (String userName, String password) {
        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        User user = new User(); // 새로운 유저 객채를 만들기 위함

    }
}
