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

    /**
     * {@code Unsafe} 对象
     */
    private static final Unsafe UNSAFE;

    /**
     * {@link Value#value} 偏移量
     */
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
        // 插入StoreStore内存屏障,保证之前写入的数据对其它操作内存可见性.
        UNSAFE.putOrderedLong(this, VALUE_OFFSET, initialValue);
    }
}

/**
 * 填充CPU缓存行,避免多线程修改互相独立的变量时，如果这些变量共享同一个缓存行，就会无意中影响彼此的性能.
 * 64字节的缓存行中,前后个7个填充,保证数据在任何被切割情况下都不会被影响.
 */
class RhsPadding extends Value {
    /**
     * 右填充
     */
    protected long p9, p10, p11, p12, p13, p14, p15;
}

class Value extends LhsPadding {
    /**
     * 保证该值的内存可见性
     */
    protected volatile long value;
}

class LhsPadding {
    /**
     * 左填充
     */
    protected long p1, p2, p3, p4, p5, p6, p7;
}
