package org.lucas.disruptor.util;

import org.lucas.disruptor.Sequence;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * @Author: shaw
 * @Date: 2019/5/17 13:02
 */
public final class Util {

    private static final Unsafe THE_UNSAFE;

    static {
        try {
            // 根据安全策略, 获取 Unsafe
            final PrivilegedExceptionAction<Unsafe> action = () -> {
                // 通过反射进行加载
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return (Unsafe) theUnsafe.get(null);
            };
            THE_UNSAFE = AccessController.doPrivileged(action);
        } catch (final Exception e) {
            throw new RuntimeException("不能加载 Unsafe ", e);
        }
    }

    /**
     * @return {@code Unsafe}
     */
    public static Unsafe getUnsafe() {
        return THE_UNSAFE;
    }

    /**
     * @see #getMinimumSequence(Sequence[], long)
     */
    public static long getMinimumSequence(final Sequence[] sequences) {
        return getMinimumSequence(sequences, Long.MAX_VALUE);
    }

    /**
     * 比较计数器中最小的游标
     *
     * @param sequences 计数器
     * @param minimum   被比较的游标
     * @return 最小游标值
     */
    public static long getMinimumSequence(final Sequence[] sequences, long minimum) {
        for (int i = 0, n = sequences.length; i < n; i++) {
            long value = sequences[i].get();
            minimum = Math.min(minimum, value);
        }
        return minimum;
    }

    /**
     * 计算所提供整数的log以2为底的对数
     *
     * @param i 值
     * @return 唯一次数
     */
    public static int log2(int i) {
        int r = 0;
        while ((i >>= 1) != 0) {
            ++r;
        }
        return r;
    }

}
