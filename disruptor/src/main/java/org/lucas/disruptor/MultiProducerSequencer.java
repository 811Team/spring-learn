package org.lucas.disruptor;

import org.lucas.disruptor.util.Util;
import sun.misc.Unsafe;

public final class MultiProducerSequencer extends AbstractSequencer {

    private static final Unsafe UNSAFE = Util.getUnsafe();

    /**
     * 起始偏移量
     */
    private static final long BASE = UNSAFE.arrayBaseOffset(int[].class);

    /**
     * 增量偏移量
     */
    private static final long SCALE = UNSAFE.arrayIndexScale(int[].class);

}
