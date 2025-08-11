package com.yeeun.yeeunblog.repository;

import com.yeeun.yeeunblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // 로그인 시 사용자 찾기 (Optional 은 조회 시 결과값이 없을 수도 있는 경우를 방지 가입하지 않은 회원
    boolean existsByUsername(String username);
    // existBy 데이터 중복 찾기 (조건은 만족하는 데이터가 1개라도 있으면 스캔 종료
}
