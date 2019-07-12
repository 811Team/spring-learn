package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/17 10:33
 */
@FunctionalInterface
public interface Cursored {

    /**
     * @return 当前游标
     */
    long getCursor();

}
