package org.lucas.disruptor;

import org.lucas.disruptor.util.Util;
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
        // 不保证写入后的数据可见性。
        UNSAFE.putOrderedLong(this, VALUE_OFFSET, initialValue);
        // 插入StoreStore内存屏障,
    }

    /**
     * @return 获取序列值
     */
    public long get() {
        return value;
    }

    /**
     * 设置一个指定值,插入 Store/Store 屏障,但不保证写入的值会立即被其它线程发现。
     *
     * @param value 新的序列值
     */
    public void set(final long value) {
        UNSAFE.putOrderedLong(this, VALUE_OFFSET, value);
        // 插入StoreStore内存屏障,
    }

    /**
     * 以 volatile 方式写入序列号,插入 Store/Load 屏障.
     *
     * @param value 新的序列值
     */
    public void setVolatile(final long value) {
        UNSAFE.putLongVolatile(this, VALUE_OFFSET, value);
        // 插入 Store/Load 屏障
    }

    /**
     * 执行一个 CAS 操作.
     *
     * @param expectedValue 预期值
     * @param newValue      需要更新的值
     * @return {@code true} 更新成功
     */
    public boolean compareAndSet(final long expectedValue, final long newValue) {
        return UNSAFE.compareAndSwapLong(this, VALUE_OFFSET, expectedValue, newValue);
    }

    public long incrementAndGet() {
        return addAndGet(1L);
    }

    public long addAndGet(final long increment) {
        // 当前值
        long currentValue;
        // 设置的值
        long newValue;
        do {
            currentValue = get();
            newValue = currentValue + increment;
        }
        // CAS 操作
        while (!compareAndSet(currentValue, newValue));
        return newValue;
    }

    @Override
    public String toString() {
        return Long.toString(get());
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
