package com.yeeun.yeeunblog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment { // 댓글 목록 불러오기, 작성수정삭제 기능, 대댓글 구조 표현

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now(); // 댓글 작성 시간
    private LocalDateTime updatedAt; // 댓글 수정 시간

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // name은 DB에서 외래키 컬럼의 이름 지절
    private User user;

    // 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private StudyPost studyPost;

    // 대댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commet_id")
    private Comment comment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // 댓글 수정 시 호출
    public void updateComment(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

}
