package org.lucas.util.concurrent;

/**
 * 根据执行结果,回调接口
 */
public interface ListenableFutureCallback<T> extends SuccessCallback<T>, FailureCallback {
}
