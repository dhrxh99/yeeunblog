package com.yeeun.yeeunblog.controller;

import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PostingController {

    private final StudyPostRepository studyPostRepository;

    @GetMapping("/api/posting") // 게시글 작성 페이지
    public ResponseEntity<StudyPost> newPosting (
                              @RequestParam (required = false) String category) {

        StudyPost post = new StudyPost();
        post.setCategory(category);

        return ResponseEntity.ok(post); // JSON 데이터 반환
    }

//    @PostMapping("/save-posting")
//    public String savePosting (@RequestBody StudyPost studyPost,
//                               RedirectAttributes ra) {
//
//        if (studyPost.getTitle() == null || studyPost.getTitle().isBlank()
//                || studyPost.getContent() == null || studyPost.getContent().isBlank()) {
//            ra.addFlashAttribute("error", "제목과 내용을 입력하세요.");
//            // 선택된 카테고리 유지해서 다시 작성 화면으로
//            String q = (studyPost.getCategory() != null) ? "?category=" + studyPost.getCategory() : "";
//            return "redirect:/posting" + q;
//        }
//
//
//        String thumb = com.yeeun.yeeunblog.util.ThumbnailUtil
//                .firstImg(studyPost.getContent());
//        studyPost.setThumbnail(thumb);
//
//        StudyPost saved = studyPostRepository.save(studyPost);
//
//        return "redirect:/study/" + studyPost.getCategory();
//    }
}
