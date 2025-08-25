package com.yeeun.yeeunblog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_posts")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyPost {

    @Id // 해당 필드가 pk 라고 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk 값을 생성하는 방법
    private Long id;

    private String category;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(updatable = true)
    private LocalDateTime createdAt; // 등록 시간 필드 추가

    @Column(nullable = false)
    private long viewCount; // 조회수 컬럼 생성

    @Column
    private String thumbnail;

    @Column(nullable = false, length = 50)
    private String author;
    @Column(nullable = false, length = 4)
    private String password;
}
