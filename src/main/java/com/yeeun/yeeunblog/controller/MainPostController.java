package com.yeeun.yeeunblog.controller;

import com.yeeun.yeeunblog.domain.StudyPost;
import com.yeeun.yeeunblog.repository.CommentRepository;
import com.yeeun.yeeunblog.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class MainPostController {

    private final StudyPostRepository studyPostRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/posting") // 게시글 작성 페이지
    public String newPosting (Model model) {

        model.addAttribute("post", new StudyPost());

        return "fragment/newPosting";
    }

    @PostMapping("/save-posting")
    public String savePosting (@ModelAttribute StudyPost studyPost) {

        String thumb = com.yeeun.yeeunblog.util.ThumbnailUtil
                .firstImg(studyPost.getContent());
        studyPost.setThumbnail(thumb);

        studyPostRepository.save(studyPost);
        return "redirect:/study/" + studyPost.getCategory();
    }
}
