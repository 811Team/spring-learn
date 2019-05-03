package org.lucas.beans.factory;

/**
 * @create: 2017-11-27
 * @description:
 */
public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader classLoader);
}
