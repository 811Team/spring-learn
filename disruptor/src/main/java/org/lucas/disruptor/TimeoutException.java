package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/21 12:38
 */
public final class TimeoutException extends Exception {

    public static final TimeoutException INSTANCE = new TimeoutException();

    private TimeoutException() {
        // Singleton
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
