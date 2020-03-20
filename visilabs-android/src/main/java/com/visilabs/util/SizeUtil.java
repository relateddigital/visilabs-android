package com.visilabs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeUtil {

    private static final Pattern FILE_EXTENSION_PATTERN = Pattern.compile("(\\.[^./]+$)");

    public static String sizeSuffixUrl(String url, String sizeSuffix) {
        final Matcher matcher = FILE_EXTENSION_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.replaceFirst(sizeSuffix + "$1");
        } else {
            return url;
        }
    }
}
