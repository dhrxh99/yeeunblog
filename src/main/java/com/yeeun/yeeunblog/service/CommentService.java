package com.yeeun.yeeunblog.service;

import com.yeeun.yeeunblog.api.dto.CommentResponse;
import com.yeeun.yeeunblog.api.dto.CreateCommentRequest;
import com.yeeun.yeeunblog.api.dto.DeleteCommentRequest;
import com.yeeun.yeeunblog.api.dto.UpdateCommentRequest;
import com.yeeun.yeeunblog.domain.Comment;
import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.CommentRepository;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final StudyPostRepository postRepo;
    private final CommentRepository commentRepo;

    public List<CommentResponse> list(Long postId) {
        List<Comment> list = commentRepo.findByPost_IdOrderByCreatedAtAsc(postId);
        return list.stream().map(this::toDto).toList();
    }

    private void validatePassword(String password) {
        if (password == null || !password.matches("\\d{4}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 4자리 숫자여야 합니다.");
        }
    }

    public CommentResponse create(Long postId, CreateCommentRequest req) {
        validatePassword(req.password());
        StudyPost post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(req.author());
        c.setPassword(req.password());
        c.setContent(req.content());

        if (req.parentId() != null) {
            Comment parent = commentRepo.findById(req.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "parent not found"));

            if (!parent.getPost().getId().equals(postId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "parent/post mismatch");
            }
            c.setParent(parent);
            c.setDepth(parent.getDepth() + 1);
        } else {
            c.setDepth(0);
        }
        return toDto(commentRepo.save(c));
    }

    public CommentResponse update(Long postId, Long commentId, UpdateCommentRequest req) {
        validatePassword(req.password());
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found"));

        if (!c.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post mismatch");
        }

        if (!req.password().equals(c.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        c.setContent(req.content());
        return toDto(commentRepo.save(c));
    }

    public void delete(Long postId, Long commentId, DeleteCommentRequest req) {
        validatePassword(req.password());
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found"));

        if (!c.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post mismatch");
        }

        // 비밀번호 검증
        if (!req.password().equals(c.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        commentRepo.delete(c);
    }

    // 변환 메서드
    private CommentResponse toDto(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getAuthor(),
                c.getContent(),
                c.getCreatedAt(),
                c.getDepth(),
                c.getParent() != null ? c.getParent().getId() : null
        );
    }
}
