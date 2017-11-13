package team811.util.concurrent;

/**
 * @create: 2017-11-13
 * @description:
 */
@FunctionalInterface
public interface FailureCallback {
    void onFailure(Throwable ex);
}
