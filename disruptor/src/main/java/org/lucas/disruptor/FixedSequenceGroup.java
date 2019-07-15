package org.lucas.disruptor;

import org.lucas.disruptor.util.Util;

import java.util.Arrays;

public final class FixedSequenceGroup extends Sequence {

    private final Sequence[] sequences;

    public FixedSequenceGroup(Sequence[] sequences) {
        this.sequences = Arrays.copyOf(sequences, sequences.length);
    }

    @Override
    public long get() {
        return Util.getMinimumSequence(sequences);
    }

    @Override
    public String toString() {
        return Arrays.toString(sequences);
    }

    /**
     * 不支持
     */
    @Override
    public void set(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * 不支持
     */
    @Override
    public boolean compareAndSet(long expectedValue, long newValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * 不支持
     */
    @Override
    public long incrementAndGet() {
        throw new UnsupportedOperationException();
    }

    /**
     * 不支持
     */
    @Override
    public long addAndGet(long increment) {
        throw new UnsupportedOperationException();
    }

}
