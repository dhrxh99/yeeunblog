// com.yeeun.yeeunblog.api.CommentApiController
package com.yeeun.yeeunblog.api;

import com.yeeun.yeeunblog.api.dto.*;
import com.yeeun.yeeunblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentResponse> list(@PathVariable Long postId) {
        return commentService.list(postId);
    }

    @PostMapping
    public CommentResponse create(@PathVariable Long postId,
                                  @RequestBody CreateCommentRequest req) {
        return commentService.create(postId, req);
    }

    @PutMapping("/{commentId}")
    public CommentResponse update(@PathVariable Long postId,
                                  @PathVariable Long commentId,
                                  @RequestBody UpdateCommentRequest req) {
        return commentService.update(postId, commentId, req);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId,
                @PathVariable Long commentId,
                @RequestBody DeleteCommentRequest req) {
        commentService.delete(postId, commentId, req);
        return ResponseEntity.noContent().build();
    }

}
