package team811.beans.factory;

/**
 * 单例对象初始化信息接口
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
