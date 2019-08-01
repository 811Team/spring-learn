package org.lucas.disruptor;

import org.lucas.disruptor.util.Util;
import sun.misc.Unsafe;

import java.util.concurrent.locks.LockSupport;

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

    /**
     * 序列游标
     */
    private final Sequence gatingSequenceCache = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);

    /**
     * 有效的容量。
     */
    private final int[] availableBuffer;
    private final int indexMask;
    private final int indexShift;

    public MultiProducerSequencer(int bufferSize, final WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
        availableBuffer = new int[bufferSize];
        indexMask = bufferSize - 1;
        indexShift = Util.log2(bufferSize);
        initialiseAvailableBuffer();
    }

    /**
     * 初始化 {@link #availableBuffer}
     */
    private void initialiseAvailableBuffer() {
        for (int i = availableBuffer.length - 1; i != 0; i--) {
            setAvailableBufferValue(i, -1);
        }
        setAvailableBufferValue(0, -1);
    }

    /**
     * 通过 Store/Store 内存屏障修改序列游标的值
     *
     * @param sequence 序列值
     */
    @Override
    public void claim(long sequence) {
        // 插入 Store/Store 内存屏障
        cursor.set(sequence);
    }

    @Override
    public boolean isAvailable(long sequence) {
        return false;
    }

    @Override
    public long getHighestPublishedSequence(long nextSequence, long availableSequence) {
        return 0;
    }

    @Override
    public boolean hasAvailableCapacity(int requiredCapacity) {
        return hasAvailableCapacity(gatingSequences, requiredCapacity, cursor.get());
    }

    @Override
    public long remainingCapacity() {
        return 0;
    }

    /**
     * @see #next(int)
     */
    @Override
    public long next() {
        return next(1);
    }

    /**
     * 通过 CAS 修改序列游标值
     *
     * @param n 需要修改的值
     * @return 修改成功的值
     */
    @Override
    public long next(int n) {
        if (n < 1 || n > bufferSize) {
            throw new IllegalArgumentException("n must be > 0 and < bufferSize");
        }
        long current;
        long next;
        do {
            // 当前游标值
            current = cursor.get();
            next = current + n;

            long wrapPoint = next - bufferSize;
            long cachedGatingSequence = gatingSequenceCache.get();

            if (wrapPoint > cachedGatingSequence || cachedGatingSequence > current) {
                long gatingSequence = Util.getMinimumSequence(gatingSequences, current);

                if (wrapPoint > gatingSequence) {
                    // 添加休眠，控制高频率时 CAS 操作空转，浪费CPU资源（或使用等待策略）
                    LockSupport.parkNanos(1);
                    continue;
                }
                // 插入Store/Store内存屏障。
                gatingSequenceCache.set(gatingSequence);
            } else if (cursor.compareAndSet(current, next)) {
                break;
            }
        }
        while (true);

        return next;
    }

    @Override
    public long tryNext() throws InsufficientCapacityException {
        return 0;
    }

    @Override
    public long tryNext(int n) throws InsufficientCapacityException {
        return 0;
    }

    @Override
    public void publish(long sequence) {

    }

    @Override
    public void publish(long lo, long hi) {

    }

    /**
     * 是否还有有效容量
     *
     * @param gatingSequences  序列游标
     * @param requiredCapacity 需要容量
     * @param cursorValue      当前游标值
     * @return 是否还有有效容量
     */
    private boolean hasAvailableCapacity(Sequence[] gatingSequences, final int requiredCapacity, long cursorValue) {
        long wrapPoint = (cursorValue + requiredCapacity) - bufferSize;
        long cachedGatingSequence = gatingSequenceCache.get();

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > cursorValue) {
            long minSequence = Util.getMinimumSequence(gatingSequences, cursorValue);
            gatingSequenceCache.set(minSequence);
            if (wrapPoint > minSequence) {
                return false;
            }
        }
        return true;
    }

    /**
     * 修改 index 元素的值。
     *
     * @param index 元素索引
     * @param flag  修改的值
     */
    private void setAvailableBufferValue(int index, int flag) {
        // 第 index 的地址
        long bufferAddress = (index * SCALE) + BASE;
        // 修改 index 元素的值（插入Store/Store内存屏障）
        UNSAFE.putOrderedInt(availableBuffer, bufferAddress, flag);
    }


}
