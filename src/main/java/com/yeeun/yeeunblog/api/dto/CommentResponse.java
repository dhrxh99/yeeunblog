package com.yeeun.yeeunblog.api.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String author,
        String content,
        LocalDateTime createdAt,
        int depth,
        Long parentId
) {}
