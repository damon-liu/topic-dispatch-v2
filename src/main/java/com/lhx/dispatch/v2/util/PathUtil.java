package com.lhx.dispatch.v2.util;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

public class PathUtil {

    private static AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 地址是否匹配
     * @param pattern
     * @param path
     * @return
     */
    public static boolean isPathMatch(String pattern, String path) {
        return matcher.match(pattern, path);
    }

    /**
     * 获取地址中的key -> val
     * @param pattern
     * @param path
     * @return
     */
    public static Map<String, String> pathKeyVal(String pattern, String path ){
        Map<String, String> map = matcher.extractUriTemplateVariables(pattern,path);
        return map;
    }
}
