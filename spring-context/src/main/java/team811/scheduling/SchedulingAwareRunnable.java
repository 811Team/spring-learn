package team811.scheduling;

/**
 * 对 {@code Runnable} 扩展
 */
public interface SchedulingAwareRunnable extends Runnable {
    boolean isLongLived();
}
