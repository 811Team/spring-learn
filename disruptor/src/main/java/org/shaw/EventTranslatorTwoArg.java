package org.shaw;

/**
 * @Author: shaw
 * @Date: 2019/5/17 12:46
 */
@FunctionalInterface
public interface EventTranslatorTwoArg<T, A, B> {

    void translateTo(T event, long sequence, A arg0, B arg1);

}
