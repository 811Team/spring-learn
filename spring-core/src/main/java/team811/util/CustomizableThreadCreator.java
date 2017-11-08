package team811.util;

import team811.lang.Nullable;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程创建
 */
public class CustomizableThreadCreator implements Serializable {

    /***
     * 线程相对优先级(默认级别:5)
     */
    private int threadPriority = Thread.NORM_PRIORITY;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix;

    /**
     * 是否守护线程
     * <p>
     * 守护线程会随主线程消亡而消亡
     */
    private boolean daemon = false;

    /**
     * 所属线程组
     */
    @Nullable
    private ThreadGroup threadGroup;

    /**
     * 线程个数
     * <p>
     * 通过AtomicInteger类的原子特性,保证并发
     */
    private final AtomicInteger threadCount = new AtomicInteger(0);

    /**
     * 自定义创建线程
     *
     * @param runnable Runnable对象
     * @return Thread
     */
    public Thread createThread(Runnable runnable) {
        // 分配线程组,线程名,创建线程
        Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
        // 设置线程优先级
        thread.setPriority(getThreadPriority());
        // 是否后台线程
        thread.setDaemon(isDaemon());
        return thread;
    }

    /**
     * 获取线程名
     *
     * @return String
     */
    protected String nextThreadName() {
        return getThreadNamePrefix() + this.threadCount.incrementAndGet();
    }

    @Nullable
    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }

    public String getThreadNamePrefix() {
        return this.threadNamePrefix;
    }

    public int getThreadPriority() {
        return this.threadPriority;
    }

    public boolean isDaemon() {
        return this.daemon;
    }
}
