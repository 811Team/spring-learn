package team811.util.concurrent;

import team811.lang.Nullable;
import team811.util.Assert;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @create: 2017-11-21
 * @description:
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
     * 将回调添加到注册中心
     *
     * @param callback
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
     * 成功回调
     *
     * @param callback {@code SuccessCallback} 执行结果
     */
    private void notifySuccess(SuccessCallback<? super T> callback) {
        try {
            callback.onSuccess((T) this.result);
        } catch (Throwable ex) {
            // Ignore
        }
    }

    /**
     * 失败回调
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
     * 将给定的成功回调添加到成功注册中心
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
     * 将给定的失败回调添加到失败注册中心
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

    private enum State {NEW, SUCCESS, FAILURE}
}
