package org.lucas.util.concurrent;

/**
 * 失败回调接口
 */
@FunctionalInterface
public interface FailureCallback {
    /**
     * 失败回调方法
     *
     * @param ex 异常类型
     */
    void onFailure(Throwable ex);
}
