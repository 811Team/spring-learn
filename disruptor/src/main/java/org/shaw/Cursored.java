package org.shaw;

/**
 * @Author: shaw
 * @Date: 2019/5/17 10:33
 */
@FunctionalInterface
public interface Cursored {
    /**
     * @return 当前 cursored 的值
     */
    long getCursor();
}
