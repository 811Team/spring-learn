package org.lucas;

/**
 * @Author: shaw
 * @Date: 2019/5/17 10:01
 */
@FunctionalInterface
public interface EventFactory<T> {
    T newInstance();
}
