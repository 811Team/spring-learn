package org.lucas.beans.factory;

/**
 * Spring 容器 bean 对象初始化信息接口
 */
public interface InitializingBean {

    /**
     * 初始化方法
     */
    void afterPropertiesSet() throws Exception;

}
