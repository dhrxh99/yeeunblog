package com.yeeun.yeeunblog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThumbnailUtil {

    private static final Pattern mdImg = Pattern.compile("!\\[[^\\]]*]\\(([^)\\s]+)(?:\\s+\"[^\"]*\")?\\)");

    private static final Pattern htmlImg = Pattern.compile("<img\\s+[^>]*src=[\"']([^\"']+)[\"'][^>]*>",
            Pattern.CASE_INSENSITIVE);

    public static String firstImg (String content) {

        if (content == null || content.isBlank())
            return null;

        try {
            Document doc = Jsoup.parseBodyFragment(content);
            Element img= doc.selectFirst("img[src]");

            if (img != null) {
                String abs = img.absUrl("src");
                return (abs != null && !abs.isBlank()) ? abs : img.attr("src");
            }
        } catch (Throwable ignore) { /* jsoup 실패시 패스 */ }

        // 2) Markdown 이미지 패턴
        Matcher m = mdImg.matcher(content);
        if (m.find()) return m.group(1);

        // 3) HTML 정규식 폴백
        m = htmlImg.matcher(content);
        if (m.find()) return m.group(1);

        return null;
    }
}
