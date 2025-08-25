package com.yeeun.yeeunblog.api.dto;

public record UpdateCommentRequest(
        String content,
        String password
) {}
