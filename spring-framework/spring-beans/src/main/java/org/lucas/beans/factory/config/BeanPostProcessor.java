package org.lucas.beans.factory.config;

import org.lucas.lang.Nullable;

/**
 * Spring 容器在初始化 bean 和完成实例化实现自定义逻辑
 */
public interface BeanPostProcessor {

    /**
     * bean 在初始化前自定义逻辑
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * bean 在加载完自定义逻辑
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
