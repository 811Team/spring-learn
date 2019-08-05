package org.lucas.util;

import org.lucas.lang.Nullable;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 判断字符串是否有长度
     *
     * @param str 字符串
     * @return {@code true} 字符串不为空而且长度不为空.
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * 检查字符串中的每个字符是否有空白符
     *
     * @param str
     * @return 如果返回 {@code true} 表示没有空白符
     */
    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    /**
     * 检查字符串中的每个字符是否有空白符
     *
     * @param str 检查的字符串
     * @return 如果返回 {@code true} 表示没有空白符
     */
    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

}
