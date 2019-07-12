package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/17 12:42
 */
@FunctionalInterface
public interface EventTranslator<T> {

    void translateTo(T event, long sequence);

}
