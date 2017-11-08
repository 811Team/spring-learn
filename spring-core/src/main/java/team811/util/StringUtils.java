package team811.util;

import team811.lang.Nullable;

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
}
