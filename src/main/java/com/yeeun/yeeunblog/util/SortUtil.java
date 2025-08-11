package com.yeeun.yeeunblog.util;

import org.springframework.data.domain.Sort;

public class SortUtil {

    public static Sort resolveSort(String sort) {
        if ("views".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "viewCount");
        }

        return Sort.by(Sort.Direction.DESC, "createdAt");
    }
}
