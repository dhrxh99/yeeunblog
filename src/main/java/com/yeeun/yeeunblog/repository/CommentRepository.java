package com.yeeun.yeeunblog.repository;

import com.yeeun.yeeunblog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost_IdOrderByCreatedAtDesc(Long postId);
    long countByPost_Id(Long postId);


}
