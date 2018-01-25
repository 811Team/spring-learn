package team811.scheduling;

/**
 * 对 {@code Runnable} 扩展
 */
public interface SchedulingAwareRunnable extends Runnable {

    /**
     * 该 Runnable 是否是长期任务
     *
     * @return
     */
    boolean isLongLived();
}
