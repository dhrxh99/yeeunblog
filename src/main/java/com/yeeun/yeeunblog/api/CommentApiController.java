// com.yeeun.yeeunblog.api.CommentApiController
package com.yeeun.yeeunblog.api;

import com.yeeun.yeeunblog.api.dto.*;
import com.yeeun.yeeunblog.domain.Comment;
import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.CommentRepository;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentApiController {

    private final StudyPostRepository postRepo;
    private final CommentRepository commentRepo;

    @GetMapping
    public List<CommentResponse> list(@PathVariable Long postId) {
        List<Comment> list = commentRepo.findByPost_IdOrderByCreatedAtAsc(postId);
        java.util.Collections.reverse(list);
        return list.stream().map(this::toDto).toList();
    }

    @PostMapping
    public CommentResponse create(@PathVariable Long postId,
                                  @RequestBody CreateCommentRequest req) {
        StudyPost post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        Comment c = new Comment();
        c.setPost(post);

        long count = commentRepo.countByPost_Id(postId);
        c.setAuthor("익명" + (count + 1));
        c.setContent(req.content().trim());

        if (req.parentId() != null) {
            Comment parent = commentRepo.findById(req.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("parent not found"));
            if (!parent.getPost().getId().equals(postId)) {
                throw new IllegalArgumentException("parent/post mismatch");
            }
            c.setParent(parent);
            c.setDepth(parent.getDepth() + 1);
        } else {
            c.setDepth(0);
        }
        return toDto(commentRepo.save(c));
    }

    @PutMapping("/{commentId}")
    public CommentResponse update(@PathVariable Long postId,
                                  @PathVariable Long commentId,
                                  @RequestBody UpdateCommentRequest req) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("comment not found"));
        if (!c.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("post mismatch");
        }
        c.setContent(req.content().trim());
        return toDto(commentRepo.save(c));
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long postId, @PathVariable Long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("comment not found"));
        if (!c.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("post mismatch");
        }
        commentRepo.delete(c);
    }

    private CommentResponse toDto(Comment c) {
        return new CommentResponse(
                c.getId(), c.getAuthor(), c.getContent(), c.getCreatedAt(), c.getDepth(),
                c.getParent() != null ? c.getParent().getId() : null
        );
    }
}
