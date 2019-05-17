package org.shaw;

/**
 * @Author: shaw
 * @Date: 2019/5/17 12:50
 */
@FunctionalInterface
public interface EventTranslatorVararg<T> {

    void translateTo(T event, long sequence, Object... args);

}
