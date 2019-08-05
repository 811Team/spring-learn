package org.lucas.core.task;

import java.util.concurrent.RejectedExecutionException;

/**
 * @create: 2017-11-14
 * @description: 继承RejectedExecutionException异常
 */
public class TaskRejectedException extends RejectedExecutionException {

    public TaskRejectedException(String msg) {
        super(msg);
    }

    public TaskRejectedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
