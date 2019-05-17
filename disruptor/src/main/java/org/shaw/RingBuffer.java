package org.shaw;

import org.shaw.util.Util;
import sun.misc.Unsafe;

/**
 * @Author: shaw
 * @Date: 2019/5/17 10:04
 */
public final class RingBuffer <E> extends RingBufferFields<E> implements Cursored, EventSequencer<E>, EventSink<E> {

}

abstract class RingBufferFields<E> extends RingBufferPad{
    private static final Unsafe UNSAFE = Util.getUnsafe();
    static {
        final int scale = UNSAFE.arrayIndexScale(Object[].class);
    }
}

abstract class RingBufferPad{
    protected long p1, p2, p3, p4, p5, p6, p7;
}
