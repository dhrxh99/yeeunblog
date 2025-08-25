package com.yeeun.yeeunblog.api.dto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        String category,
        String author,
        String thumbnail,
        LocalDateTime createdAt,
        long viewCount
) {}
