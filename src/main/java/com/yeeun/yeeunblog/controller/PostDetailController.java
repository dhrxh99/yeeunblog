package com.yeeun.yeeunblog.controller;

import com.yeeun.yeeunblog.domain.Comment;
import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.CommentRepository;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostDetailController {

    private final StudyPostRepository  studyPostRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/post/{id}") // 상세 페이지 + 댓글 조회
    public String addCommentPost (@PathVariable Long id, Model model) {

        StudyPost post = studyPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + id));

        post.setViewCount(post.getViewCount() + 1);
        studyPostRepository.save(post);

        List<Comment> comments = commentRepository.findByPost_IdOrderByCreatedAtDesc(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);

        return "studyPostPage/PostDetailPage";
    }

    @PostMapping("/post/{postId}/comments") // 상세 페이지에 댓글을 작성하는 로직
    public String comment (@PathVariable Long postId,
                           @RequestParam("content") String content) {

        if (content == null || content.trim().isEmpty()) {
            // 내용이 비었으면 DB 저장 안 하고 상세 페이지로 리다이렉트
            return "redirect:/post/" + postId + "#comments";
        }

        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        long count = commentRepository.countByPost_Id(postId);
        String generatedAuthor = "익명" + (count + 1);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content.trim());
        // createdAt은 @CreatedDate로 자동 주입된다면 OK (아래 참고)
        commentRepository.save(comment);

        return "redirect:/post/" + postId + "#comments";
    }




}
