package team811.beans.factory;

/**
 * @create: 2017-11-08
 * @description:
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
