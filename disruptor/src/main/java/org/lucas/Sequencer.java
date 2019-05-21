package org.lucas;

/**
 * @Author: shaw
 * @Date: 2019/5/17 14:06
 */
public interface Sequencer extends Cursored, Sequenced {

    /**
     * 设置 -1 为序列起始.
     */
    long INITIAL_CURSOR_VALUE = -1L;

    /**
     * 声明一个序列。仅在环缓冲区初始化为特定值时使用。
     *
     * @param sequence 序列值
     */
    void claim(long sequence);

    /**
     * 确认序列是否发布有效事件并使用,非阻塞.
     *
     * @param sequence 序列值
     * @return 如果 {@code true} 已发布有效事件并使用
     */
    boolean isAvailable(long sequence);

    /**
     * 将指定序列器器添加到 Disruptor 实列中.
     *
     * @param gatingSequences 序列器
     */
    void addGatingSequences(Sequence... gatingSequences);

    /**
     * 删除指定序列器
     *
     * @param sequence 序列器
     * @return {@code true} 删除成功
     */
    boolean removeGatingSequence(Sequence sequence);

    SequenceBarrier newBarrier(Sequence... sequencesToTrack);

    long getMinimumSequence();

    long getHighestPublishedSequence(long nextSequence, long availableSequence);

    <T> EventPoller<T> newPoller(DataProvider<T> provider, Sequence... gatingSequences);

}
