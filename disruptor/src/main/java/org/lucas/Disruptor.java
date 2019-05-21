package org.lucas;

import java.util.concurrent.ThreadFactory;

/**
 * @Author: shaw
 * @Date: 2019/5/17 9:48
 */
public class Disruptor<T> {

    public Disruptor(final EventFactory<T> eventFactory, final int ringBufferSize, final ThreadFactory threadFactory) {
        this(RingBuffer.createMultiProducer(eventFactory, ringBufferSize), new BasicExecutor(threadFactory));
    }

}
