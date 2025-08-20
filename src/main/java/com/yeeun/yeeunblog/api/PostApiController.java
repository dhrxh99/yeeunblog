// com.yeeun.yeeunblog.api.PostApiController
package com.yeeun.yeeunblog.api;

import com.yeeun.yeeunblog.api.dto.*;
import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import com.yeeun.yeeunblog.util.SortUtil;
import com.yeeun.yeeunblog.util.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final StudyPostRepository postRepo;

    // 게시글 정렬
    @GetMapping
    public PostPageResponse list(
            @RequestParam(required=false) String category,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, SortUtil.resolveSort(sort));
        Page<StudyPost> p = (category == null || category.isBlank())
                ? postRepo.findAll(pageable)
                : postRepo.findByCategoryIgnoreCase(category, pageable);

        List<PostResponse> items = p.getContent().stream().map(this::toDto).toList();
        return new PostPageResponse(items, p.getNumber(), p.getTotalPages(), p.getTotalElements());
    }

    // 단건 조회
    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        StudyPost post = postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("post not found: " + id));
        return toDto(post);
    }

    // 조회수 +1 (선택)
    @PostMapping("/{id}/view")
    public void addView(@PathVariable Long id) {
        StudyPost post = postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("post not found: " + id));
        post.setViewCount(post.getViewCount() + 1);
        postRepo.save(post);
    }

    // 새 게시글 저장
    @PostMapping
    public PostResponse create(@RequestBody CreatePostRequest req) {
        if (req.title() == null || req.title().isBlank() ||
                req.content() == null || req.content().isBlank()) {
            throw new IllegalArgumentException("title/content required");
        }
        StudyPost post = new StudyPost();
        post.setTitle(req.title());
        post.setContent(req.content());
        post.setCategory(req.category());
        post.setViewCount(0);
        post.setThumbnail(ThumbnailUtil.firstImg(req.content())); // ✅ 첫 이미지 추출

        StudyPost saved = postRepo.save(post);
        return toDto(saved);
    }

    // 수정
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id, @RequestBody UpdatePostRequest req) {
        StudyPost post = postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("post not found: " + id));
        post.setTitle(req.title());
        post.setContent(req.content());
        post.setCategory(req.category());
        post.setThumbnail(ThumbnailUtil.firstImg(req.content()));
        return toDto(postRepo.save(post));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postRepo.deleteById(id);
    }

    private PostResponse toDto(StudyPost p) {
        return new PostResponse(
                p.getId(), p.getTitle(), p.getContent(), p.getCategory(), p.getThumbnail(),
                p.getCreatedAt(), p.getViewCount()
        );
    }
}

