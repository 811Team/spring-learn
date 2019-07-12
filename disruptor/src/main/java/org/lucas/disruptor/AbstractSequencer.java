package org.lucas.disruptor;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class AbstractSequencer implements Sequencer {

    /**
     * 原子修改器，修改 gatingSequences 属性
     */
    private static final AtomicReferenceFieldUpdater<AbstractSequencer, Sequence[]> SEQUENCE_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(AbstractSequencer.class, Sequence[].class, "gatingSequences");

    /**
     * 队列大小
     */
    protected final int bufferSize;

    protected final WaitStrategy waitStrategy;

    /**
     * 游标计数器
     */
    protected final Sequence cursor = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);

    /**
     * 计数器
     */
    protected volatile Sequence[] gatingSequences = new Sequence[0];

    public AbstractSequencer(int bufferSize, WaitStrategy waitStrategy) {
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }
        this.bufferSize = bufferSize;
        this.waitStrategy = waitStrategy;
    }

    @Override
    public final long getCursor() {
        return cursor.get();
    }

    @Override
    public final int getBufferSize() {
        return bufferSize;
    }

}
