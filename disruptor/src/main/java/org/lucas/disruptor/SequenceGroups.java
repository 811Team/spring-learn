package org.lucas.disruptor;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static java.util.Arrays.copyOf;

class SequenceGroups {

    /**
     * 扩展序列游标容量
     *
     * @param holder         扩展序列游标容量的对象
     * @param updater        原子更新器
     * @param cursor         当前游标
     * @param sequencesToAdd 扩展的序列游标
     */
    static <T> void addSequences(final T holder, final AtomicReferenceFieldUpdater<T, Sequence[]> updater,
                                 final Cursored cursor, final Sequence... sequencesToAdd) {
        long cursorSequence;
        Sequence[] updatedSequences;
        Sequence[] currentSequences;

        do {
            // 获取当前对象序列游标
            currentSequences = updater.get(holder);
            // 计算新的序列游标长度
            updatedSequences = copyOf(currentSequences, currentSequences.length + sequencesToAdd.length);
            // 当前游标位置
            cursorSequence = cursor.getCursor();


            int index = currentSequences.length;
            for (Sequence sequence : sequencesToAdd) {
                // 设置当前游标位置
                sequence.set(cursorSequence);
                // 新的序列游标
                updatedSequences[index++] = sequence;
            }
        }
        // 原子性修改 holder 对象的序列游标。
        while (!updater.compareAndSet(holder, currentSequences, updatedSequences));

        // 获取当前游标。
        cursorSequence = cursor.getCursor();
        for (Sequence sequence : sequencesToAdd) {
            sequence.set(cursorSequence);
        }
    }

    /**
     * 删除匹配的序列游标
     *
     * @param holder          删除序列游标的对象
     * @param sequenceUpdater 原子更新器
     * @param sequence        需要删除的序列游标
     * @return {@code true} 删除成功
     */
    static <T> boolean removeSequence(final T holder, final AtomicReferenceFieldUpdater<T, Sequence[]> sequenceUpdater,
                                      final Sequence sequence) {
        int numToRemove;
        Sequence[] oldSequences;
        Sequence[] newSequences;

        do {
            oldSequences = sequenceUpdater.get(holder);

            // 需要删除的个数
            numToRemove = countMatching(oldSequences, sequence);

            if (0 == numToRemove) {
                break;
            }

            final int oldSize = oldSequences.length;
            newSequences = new Sequence[oldSize - numToRemove];

            for (int i = 0, pos = 0; i < oldSize; i++) {
                final Sequence testSequence = oldSequences[i];
                if (sequence != testSequence) {
                    newSequences[pos++] = testSequence;
                }
            }
        }
        while (!sequenceUpdater.compareAndSet(holder, oldSequences, newSequences));

        return numToRemove != 0;
    }

    /**
     * 数组中包含多少个需要匹配的值
     *
     * @param values  数组
     * @param toMatch 匹配的值
     * @return 匹配到的个数
     */
    private static <T> int countMatching(T[] values, final T toMatch) {
        int numToRemove = 0;
        for (T value : values) {
            if (value == toMatch) {
                numToRemove++;
            }
        }
        return numToRemove;
    }

}
