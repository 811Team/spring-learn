package org.lucas.disruptor;

/**
 * @Author: shaw
 * @Date: 2019/5/17 12:44
 */
@FunctionalInterface
public interface EventTranslatorOneArg<T, A> {

    void translateTo(T event, long sequence, A arg0);

}
