package org.lucas.beans.factory;

/**
 * @create: 2017-11-08
 * @description:
 */
public interface BeanNameAware extends Aware {
    void setBeanName(String name);
}
