package org.lucas.beans.factory;

public interface BeanFactoryAware {

    /**
     * 所Spring 框架会调用 setBeanFactory(BeanFactory beanFactory) 方法
     * 把 Spring BeanFactory(存放 bean 的容器) 注入该 Bean。
     *
     * @param beanFactory
     * @throws BeansException
     */
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
