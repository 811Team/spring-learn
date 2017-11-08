package team811.util;

import team811.lang.Nullable;

/**
 * @create: 2017-06-30
 * @description:
 */
public abstract class Assert {

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 如果字符串长度为空,则抛出异常信息
     *
     * @param text    字符串
     * @param message 异常信息
     */
    public static void hasLength(@Nullable String text, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }
}
