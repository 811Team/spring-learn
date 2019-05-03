package org.lucas.util.concurrent;

import org.lucas.lang.Nullable;
import org.lucas.util.Assert;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.FutureTask;

/**
 * 为 {@code ListenableFutureTask} 提供成功、失败回调队列
 */
public class ListenableFutureCallbackRegistry<T> {

    /** 成功任务队列 */
    private final Queue<SuccessCallback<? super T>> successCallbacks = new LinkedList<>();

    /** 失败任务队列 */
    private final Queue<FailureCallback> failureCallbacks = new LinkedList<>();

    /** 状态(初始化状态) */
    private State state = State.NEW;

    /** 返回结果 */
    @Nullable
    private Object result;

    /** 锁对象 */
    private final Object mutex = new Object();

    /**
     * 如果 {@code ListenableFutureTask} 初始化，则将 {@code callback}
     * 添加到 {@code successCallbacks} 和 {@code failureCallbacks} 队列；
     * <p>
     * 如果 {@code ListenableFutureTask} 任务结束调用该方法则调用 {@code successCallbacks} 队列中的内容；
     * 如果 {@code ListenableFutureTask} 任务发生异常时调用该方法则调用 {@code failureCallbacks} 队列中的内容；
     *
     * @param callback 回调内容
     */
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        Assert.notNull(callback, "'callback' must not be null");
        synchronized (this.mutex) {
            switch (this.state) {
                case NEW:
                    // 添加到成功任务队列
                    this.successCallbacks.add(callback);
                    // 添加到失败任务队列
                    this.failureCallbacks.add(callback);
                    break;
                case SUCCESS:
                    // 回调成功方法
                    notifySuccess(callback);
                    break;
                case FAILURE:
                    // 回调失败方法
                    notifyFailure(callback);
                    break;
            }
        }
    }

    /**
     * 成功回调内容
     *
     * @param callback {@code SuccessCallback} 执行结果
     * @see SuccessCallback#onSuccess(Object)
     */
    private void notifySuccess(SuccessCallback<? super T> callback) {
        try {
            callback.onSuccess((T) this.result);
        } catch (Throwable ex) {
            // Ignore
        }
    }

    /**
     * 失败回调内容
     *
     * @param callback {@code FailureCallback} 执行结果
     */
    private void notifyFailure(FailureCallback callback) {
        Assert.state(this.result instanceof Throwable, "No Throwable result for failure state");
        try {
            callback.onFailure((Throwable) this.result);
        } catch (Throwable ex) {
            // Ignore
        }
    }

    /**
     * 如果 {@code ListenableFutureTask} 初始化，则将 {@code callback}
     * 添加到 {@code successCallbacks}；
     * 如果{@code ListenableFutureTask} 结束后，调用该方法，则调用成功内容
     *
     * @param callback {@code SuccessCallback}
     */
    public void addSuccessCallback(SuccessCallback<? super T> callback) {
        Assert.notNull(callback, "'callback' must not be null");
        synchronized (this.mutex) {
            switch (this.state) {
                case NEW:
                    // 添加到成功任务队列
                    this.successCallbacks.add(callback);
                    break;
                case SUCCESS:
                    // 回调成功方法
                    notifySuccess(callback);
                    break;
            }
        }
    }

    /**
     * 如果 {@code ListenableFutureTask} 初始化，则将 {@code callback}
     * 添加到 {@code failureCallbacks}；
     * 如果{@code ListenableFutureTask} 发生异常，调用该方法，则调用失败内容
     *
     * @param callback {@code FailureCallback}
     */
    public void addFailureCallback(FailureCallback callback) {
        Assert.notNull(callback, "'callback' must not be null");
        synchronized (this.mutex) {
            switch (this.state) {
                case NEW:
                    // 添加到失败任务队列
                    this.failureCallbacks.add(callback);
                    break;
                case FAILURE:
                    // 回调失败方法
                    notifyFailure(callback);
                    break;
            }
        }
    }

    /**
     * 当 {@code FutureTask} 结束时没异常时，调用该方法
     *
     * @param result {@link FutureTask#get()}
     * @see #notifySuccess(SuccessCallback)
     */
    public void success(@Nullable T result) {
        synchronized (this.mutex) {
            this.state = State.SUCCESS;
            this.result = result;
            while (!this.successCallbacks.isEmpty()) {
                // 返回成功队列中的第一个 SuccessCallback 对象，并删除
                notifySuccess(this.successCallbacks.poll());
            }
        }
    }

    /**
     * 当 {@code FutureTask} 发生错误时，调用该方法
     *
     * @param ex 错误信息
     */
    public void failure(Throwable ex) {
        synchronized (this.mutex) {
            this.state = State.FAILURE;
            this.result = ex;
            while (!this.failureCallbacks.isEmpty()) {
                notifyFailure(this.failureCallbacks.poll());
            }
        }
    }

    private enum State {NEW, SUCCESS, FAILURE}
}
