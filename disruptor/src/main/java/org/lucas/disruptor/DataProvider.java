package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/17 11:15
 */
@FunctionalInterface
public interface DataProvider<T> {

    T get(long sequence);

}
