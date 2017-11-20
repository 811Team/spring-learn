package team811.util;

import team811.Log;
import team811.LogFactory;

import java.io.Serializable;

/**
 * 通过该抽象类,可实现任务并发量的控制
 */
public abstract class ConcurrencyThrottleSupport implements Serializable {

    /** 不允许并发常量 */
    public static final int NO_CONCURRENCY = 0;

    /** 允许并发常量 */
    public static final int UNBOUNDED_CONCURRENCY = -1;

    /**
     * 日志对象
     * <p>
     * 序列化时,忽略该对象
     */
    protected transient Log logger = LogFactory.getLog(getClass());

    /** 锁对象 */
    private transient Object monitor = new Object();

    /** 并发量设置 */
    private int concurrencyLimit = UNBOUNDED_CONCURRENCY;

    /** 实际已经运行的并发数 */
    private int concurrencyCount = 0;

    /**
     * 任务运行前,通过调用该方法,控制并发量
     */
    protected void beforeAccess() {
        // 如果不允许并发设置,直接抛出异常
        if (this.concurrencyLimit == NO_CONCURRENCY) {
            throw new IllegalStateException(
                    "Currently no invocations allowed - concurrency limit set to NO_CONCURRENCY");
        }
        if (this.concurrencyLimit > 0) {
            synchronized (this.monitor) {
                // 判断线程是否发生异常标识
                boolean interrupted = false;
                /**
                 * 判断当前线程并发个数是否超过设置并发量
                 */
                // 如果超过,则无限循环此方法
                while (this.concurrencyCount >= this.concurrencyLimit) {
                    // 中断抛出异常
                    if (interrupted) {
                        throw new IllegalStateException("Thread was interrupted while waiting for invocation access, " +
                                "but concurrency limit still does not allow for entering");
                    }
                    try {
                        // 线程等待
                        this.monitor.wait();
                    } catch (InterruptedException ex) {
                        // 发生异常,尝试终止线程
                        Thread.currentThread().interrupt();
                        interrupted = true;
                    }
                }
                this.concurrencyCount++;
            }
        }
    }

    /**
     * 任务运行完毕,通过调用该方法释放wait()的线程
     */
    protected void afterAccess() {
        if (this.concurrencyLimit >= 0) {
            synchronized (this.monitor) {
                this.concurrencyCount--;
                if (logger.isDebugEnabled()) {
                    logger.debug("Returning from throttle at concurrency count " + this.concurrencyCount);
                }
                this.monitor.notify();
            }
        }
    }

    /**
     * 判断设置的并发量是否大于0
     */
    public boolean isThrottleActive() {
        return (this.concurrencyLimit >= 0);
    }
}
