package org.shaw;

import org.shaw.util.Util;
import sun.misc.Unsafe;

/**
 * @Author: shaw
 * @Date: 2019/5/17 10:04
 */
public final class RingBuffer<E> extends RingBufferFields<E> implements Cursored, EventSequencer<E>, EventSink<E> {

}

abstract class RingBufferFields<E> extends RingBufferPad {

    private static final Unsafe UNSAFE = Util.getUnsafe();

    /**
     * 指针偏移量
     */
    private static final int REF_ELEMENT_SHIFT;

    private static final long REF_ARRAY_BASE;

    private static final int BUFFER_PAD;

    static {
        // 获取对象的增量长度.访问该类型为 Object[] 的第N个元素的话,偏移量offset应该是arrayOffset+N*arrayScale。
        final int scale = UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            // 2的2次幂
            REF_ELEMENT_SHIFT = 2;
        } else if (8 == scale) {
            // 2的3次幂
            REF_ELEMENT_SHIFT = 3;
        } else {
            throw new IllegalStateException("未确定指针大小.");
        }
        BUFFER_PAD = 128 / scale;
        // Object[] 的基本偏移量
        REF_ARRAY_BASE = UNSAFE.arrayBaseOffset(Object[].class) + 128;
    }

    RingBufferFields(EventFactory<E> eventFactory, Sequencer sequencer) {

    }
}

abstract class RingBufferPad {
    protected long p1, p2, p3, p4, p5, p6, p7;
}
