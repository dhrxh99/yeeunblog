package com.yeeun.yeeunblog.api.dto;

public record CreateCommentRequest(
        String content,
        String author,
        String password,
        Long parentId
) {}
