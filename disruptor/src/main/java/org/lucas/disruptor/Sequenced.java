package org.lucas.disruptor;

/**
 * 序列器
 *
 * @Author: shaw
 * @Date: 2019/5/17 11:20
 */
public interface Sequenced {
    /**
     * @return 数据结构大小
     */
    int getBufferSize();

    /**
     * 是否能分配一个排序器.
     *
     * @param requiredCapacity 序列容量
     * @return 如果 {@code true} 则能够分配.
     */
    boolean hasAvailableCapacity(int requiredCapacity);

    /**
     * @return 此排序器的剩余容量
     */
    long remainingCapacity();

    /**
     * @return 按顺序获取下一个事件序列号
     */
    long next();

    /**
     * @return 声明接下来要发布 n 个事件。用于批量事件生成的。
     */
    long next(int n);

    /**
     * 尝试按顺序发布下个事件
     *
     * @return
     * @throws InsufficientCapacityException
     */
    long tryNext() throws InsufficientCapacityException;

    long tryNext(int n) throws InsufficientCapacityException;

    void publish(long sequence);

    void publish(long lo, long hi);
}
