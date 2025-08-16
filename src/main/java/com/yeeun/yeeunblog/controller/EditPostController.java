package com.yeeun.yeeunblog.controller;

import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.CommentRepository;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class EditPostController {

    private final CommentRepository commentRepository;
    private final StudyPostRepository studyPostRepository;

    // 수정 폼 띄우기
    @GetMapping("/{id}/edit")
    public String editForm (@PathVariable Long id, Model model) {

        StudyPost  post = studyPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다: " + id));
        model.addAttribute("post", post);

        return "fragment/editPostPage";
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String updatePost (@PathVariable Long id,
                              @ModelAttribute StudyPost updatePost,
                              RedirectAttributes ra) {

        StudyPost post = studyPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다: " + id));

        if (updatePost.getTitle() == null || updatePost.getTitle().isBlank()
                || updatePost.getContent() == null || updatePost.getContent().isBlank()) {
            ra.addFlashAttribute("error", "제목과 내용을 입력하세요.");
            return "redirect:/post/" + id + "/edit";
        }

        post.setTitle(updatePost.getTitle()); // 수정한 title을 다시 저장하는 로직
        post.setContent(updatePost.getContent());
        String thumb = com.yeeun.yeeunblog.util.ThumbnailUtil
                .firstImg(post.getContent());
        post.setThumbnail(thumb); // 이미지 수정 시 썸네일도 같이 변경
        post.setCategory(updatePost.getCategory());

        studyPostRepository.save(post);

        ra.addFlashAttribute("message", "수정되었습니다.");

        return "redirect:/post/" + id; // 수정 후 홈으로 리다이렉트
    }

    // 삭제 기능
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id,
                             RedirectAttributes ra) {

        if (!studyPostRepository.existsById(id)) {
            ra.addFlashAttribute("error", "이미 삭제되었거나 없는 게시글입니다.");

            return "redirect:/post/" + id;
        }

        studyPostRepository.deleteById(id);
        ra.addFlashAttribute("message", "삭제되었습니다.");

        return "redirect:/"; // 브라우저를 다른 URL로 이동시키는 명령어
    }

    // 댓글 수정
    @PostMapping("/{postId}/comments/{commentId}/edit")
    public String updateComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @RequestParam("content") String content,
                                RedirectAttributes ra) {

        if (content == null || content.trim().isEmpty()) {
            ra.addFlashAttribute("error", "댓글 내용을 입력하세요.");
            return "redirect:/post/" + postId + "#comments";
        }

        return commentRepository.findById(commentId)
                .map(c -> {
                    // (선택) 안전 체크: 다른 글의 댓글이면 막기
                    if (!c.getPost().getId().equals(postId)) {
                        ra.addFlashAttribute("error", "잘못된 요청입니다.");
                        return "redirect:/post/" + postId + "#comments";
                    }
                    c.setContent(content.trim());
                    commentRepository.save(c);
                    return "redirect:/post/" + postId + "#comments";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "이미 삭제되었거나 없는 댓글입니다.");
                    return "redirect:/post/" + postId + "#comments";
                });
    }

    // 댓글 삭제
    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                RedirectAttributes ra) {

        return commentRepository.findById(commentId)
                .map(c -> {
                    // (선택) 안전 체크: 다른 글의 댓글이면 막기
                    if (!c.getPost().getId().equals(postId)) {
                        ra.addFlashAttribute("error", "잘못된 요청입니다.");
                        return "redirect:/post/" + postId + "#comments";
                    }
                    commentRepository.delete(c);
                    ra.addFlashAttribute("message", "댓글이 삭제되었습니다.");
                    return "redirect:/post/" + postId + "#comments";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "이미 삭제되었거나 없는 댓글입니다.");
                    return "redirect:/post/" + postId + "#comments";
                });
    }

    @Data
    public static class UpdatePostRequest {
        private String title;
        private String content;
        private String category;
    }

}

