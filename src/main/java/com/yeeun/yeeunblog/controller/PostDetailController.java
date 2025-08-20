//package com.yeeun.yeeunblog.controller;
//
//import com.yeeun.yeeunblog.domain.Comment;
//import com.yeeun.yeeunblog.domain.StudyPost;
//import com.yeeun.yeeunblog.repository.CommentRepository;
//import com.yeeun.yeeunblog.repository.StudyPostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class PostDetailController {
//
//    private final StudyPostRepository  studyPostRepository;
//    private final CommentRepository commentRepository;
//
//    // 상세 페이지 + 댓글 조회
//    @GetMapping("/post/{id}")
//    public String viewPost (@PathVariable Long id, Model model) {
//
//        StudyPost post = studyPostRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + id));
//
//        post.setViewCount(post.getViewCount() + 1);
//        studyPostRepository.save(post);
//
//        List<Comment> comments = commentRepository.findByPost_IdOrderByCreatedAtAsc(id);
//        model.addAttribute("post", post);
//        model.addAttribute("comments", comments);
//
//        return "studyPostPage/PostDetailPage";
//    }
//
//    // 상세 페이지에 댓글/ 대댓글 작성하는 (통합)
//    @PostMapping("/post/{postId}/comments")
//    public String comment (@PathVariable Long postId,
//                           @RequestParam(value = "parentId", required = false) Long parentId,
//                           @RequestParam("content") String content) {
//
//        if (content == null || content.trim().isEmpty()) {
//            // 내용이 비었으면 DB 저장 안 하고 상세 페이지로 리다이렉트
//            return "redirect:/post/" + postId + "#comments";
//        }
//
//        StudyPost post = studyPostRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));
//
//        long count = commentRepository.countByPost_Id(postId);
//        String generatedAuthor = "익명" + (count + 1);
//
//        Comment comment = new Comment();
//        comment.setPost(post);
//        comment.setAuthor(generatedAuthor);
//        comment.setContent(content.trim());
//
//        if (parentId != null) {
//            Comment parent = commentRepository.findById(parentId)
//                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글 없음: " + parentId));
//            if (!parent.getPost().getId().equals(postId)) {
//                throw new IllegalArgumentException("부모 댓글/게시글 불일치");
//            }
//            comment.setParent(parent);
//            comment.setDepth(parent.getDepth() + 1);
//        } else {
//            comment.setDepth(0);
//        }
//
//        commentRepository.save(comment);
//
//        return "redirect:/post/" + postId + "#comments";
//    }
//
//}
