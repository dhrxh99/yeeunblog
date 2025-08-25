// com.yeeun.yeeunblog.api.PostApiController
package com.yeeun.yeeunblog.api;

import com.yeeun.yeeunblog.api.dto.*;
import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.service.StudyPostService;
import com.yeeun.yeeunblog.util.SortUtil;
import com.yeeun.yeeunblog.util.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final StudyPostService postService;

    // 게시글 정렬
    @GetMapping
    public PostPageResponse list(
            @RequestParam(required=false) String category,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, SortUtil.resolveSort(sort));
        Page<StudyPost> p = postService.getSortedPostsByCategory(category, pageable);

        List<PostResponse> items = p.getContent().stream().map(this::toDto).toList();
        return new PostPageResponse(items, p.getNumber(), p.getTotalPages(), p.getTotalElements());
    }

    // 조회
    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return toDto(postService.getPost(id));
    }

    // 새 게시글 작성
    @PostMapping
    public PostResponse create(@RequestBody CreatePostRequest req) {
        StudyPost post = new StudyPost();

        post.setTitle(req.title());
        post.setContent(req.content());
        post.setCategory(req.category());
        post.setViewCount(0);
        post.setThumbnail(ThumbnailUtil.firstImg(req.content())); // 첫 이미지 추출
        post.setAuthor(req.author());
        post.setPassword(req.password());

        return toDto(postService.createPost(post));
    }

    // 수정
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id, @RequestBody UpdatePostRequest req) {
        StudyPost updated = postService.updatePost(
                id,
                req.title(),
                req.content(),
                req.category(),
                req.password()
        );
        return toDto(updated);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody DeletePostRequest req) {
        postService.deletePost(id, req.password());
        return ResponseEntity.noContent().build();
    }

    private PostResponse toDto(StudyPost p) {
        return new PostResponse(
                p.getId(),
                p.getTitle(),
                p.getContent(),
                p.getCategory(),
                p.getThumbnail(),
                p.getCreatedAt(),
                p.getViewCount()
        );
    }
}

