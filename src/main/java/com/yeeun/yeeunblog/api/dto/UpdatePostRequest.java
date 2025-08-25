package com.yeeun.yeeunblog.api.dto;

public record UpdatePostRequest(
        String title,
        String content,
        String category,
        String password
) {}
