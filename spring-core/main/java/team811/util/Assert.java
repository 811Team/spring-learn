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
}
