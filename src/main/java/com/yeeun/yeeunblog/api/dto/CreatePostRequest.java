package com.yeeun.yeeunblog.api.dto;

public record CreatePostRequest(
        String title,
        String content,
        String category,
        String author,
        String password
) {}
