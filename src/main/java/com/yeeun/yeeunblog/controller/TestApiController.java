package com.yeeun.yeeunblog.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestApiController {

    @GetMapping("/api/test-log")
    public String testLog(@RequestParam(defaultValue = "hello") String msg) {
        System.out.println("📌 [GET 요청 도착] msg=" + msg);
        return "서버 로그 확인: " + msg;
    }
}

