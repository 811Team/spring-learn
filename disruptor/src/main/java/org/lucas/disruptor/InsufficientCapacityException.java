package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/17 11:46
 */
public final class InsufficientCapacityException extends Exception {

    public static final InsufficientCapacityException INSTANCE = new InsufficientCapacityException();

    private InsufficientCapacityException() {
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
