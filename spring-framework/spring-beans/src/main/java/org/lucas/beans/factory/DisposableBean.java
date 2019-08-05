package org.lucas.beans.factory;

/**
 * 单例对象销毁标记接口
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
