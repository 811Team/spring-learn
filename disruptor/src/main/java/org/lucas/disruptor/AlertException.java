package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/21 12:39
 */
public final class AlertException extends Exception {

    public static final AlertException INSTANCE = new AlertException();

    private AlertException() {
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
