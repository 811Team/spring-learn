package org.lucas.disruptor;

import org.lucas.disruptor.util.Util;

import java.util.Arrays;
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
     * 序列游标
     */
    protected final Sequence cursor = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);

    /**
     * 序列游标数组
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

    /**
     * @see Sequencer#addGatingSequences(Sequence...)
     */
    @Override
    public final void addGatingSequences(Sequence... gatingSequences) {
        SequenceGroups.addSequences(this, SEQUENCE_UPDATER, this, gatingSequences);
    }

    /**
     * @see Sequencer#removeGatingSequence(Sequence)
     */
    @Override
    public boolean removeGatingSequence(Sequence sequence) {
        return SequenceGroups.removeSequence(this, SEQUENCE_UPDATER, sequence);
    }

    /**
     * @see Sequencer#getMinimumSequence()
     */
    @Override
    public long getMinimumSequence() {
        return Util.getMinimumSequence(gatingSequences, cursor.get());
    }

    /**
     * @see Sequencer#newBarrier(Sequence...)
     */
    @Override
    public SequenceBarrier newBarrier(Sequence... sequencesToTrack) {
        return new ProcessingSequenceBarrier(this, waitStrategy, cursor, sequencesToTrack);
    }

    /**
     * 为该序列创建一个事件轮询器，该事件轮询器将使用提供的数据提供程序和门控序列。
     *
     * @param dataProvider    当前事件轮询器的数据源
     * @param gatingSequences 序列游标
     * @return 将在此环缓冲区和提供的序列上进行门控的轮询器。
     */
    @Override
    public <T> EventPoller<T> newPoller(DataProvider<T> dataProvider, Sequence... gatingSequences) {
        return EventPoller.newInstance(dataProvider, this, new Sequence(), cursor, gatingSequences);
    }

    @Override
    public String toString() {
        return "AbstractSequencer{" +
                "waitStrategy=" + waitStrategy +
                ", cursor=" + cursor +
                ", gatingSequences=" + Arrays.toString(gatingSequences) +
                '}';
    }

}
