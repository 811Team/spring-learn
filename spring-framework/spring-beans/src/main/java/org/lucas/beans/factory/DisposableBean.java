package org.lucas.beans.factory;

/**
 * Spring 容器 bean 对象销毁标记接口
 */
public interface DisposableBean {

    /**
     * 销毁方法
     */
    void destroy() throws Exception;
}
