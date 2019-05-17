package org.shaw;

/**
 * @Author: shaw
 * @Date: 2019/5/17 10:04
 */
public final class RingBuffer <E> extends RingBufferFields<E> implements Cursored, EventSequencer<E>, EventSink<E> {

}

abstract class RingBufferFields<E> extends RingBufferPad{
    static {

    }
}

abstract class RingBufferPad{
    protected long p1, p2, p3, p4, p5, p6, p7;
}
