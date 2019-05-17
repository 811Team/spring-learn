package org.shaw;

import org.shaw.util.Util;
import sun.misc.Unsafe;

/**
 * @Author: shaw
 * @Date: 2019/5/17 14:23
 */
public class Sequence extends RhsPadding {

    /**
     * 设置起始序列值为 -1.
     */
    static final long INITIAL_VALUE = -1L;

    private static final Unsafe UNSAFE;

    private static final long VALUE_OFFSET;

    static {
        UNSAFE = Util.getUnsafe();
        try {
            // 获取属性的偏移量
            VALUE_OFFSET = UNSAFE.objectFieldOffset(Value.class.getDeclaredField("value"));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Sequence() {
        this(INITIAL_VALUE);
    }

    public Sequence(final long initialValue) {
        UNSAFE.putOrderedLong(this, VALUE_OFFSET, initialValue);
    }
}

class RhsPadding extends Value {
    protected long p9, p10, p11, p12, p13, p14, p15;
}

class Value extends LhsPadding {
    protected volatile long value;
}

class LhsPadding {
    protected long p1, p2, p3, p4, p5, p6, p7;
}
