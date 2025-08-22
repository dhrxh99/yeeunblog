package com.yeeun.yeeunblog.repository;

import com.yeeun.yeeunblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
