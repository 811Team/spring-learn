package org.lucas;

/**
 * 序列器内存屏障
 *
 * @Author: shaw
 * @Date: 2019/5/21 11:49
 */
public interface SequenceBarrier {

    long waitFor(long sequence) throws AlertException, InterruptedException, TimeoutException;

    long getCursor();

    boolean isAlerted();

    void alert();

    void clearAlert();

    void checkAlert() throws AlertException;

}
