package org.lucas.core.env;

/**
 * @create: 2018-01-26
 * @description:
 */
public interface Environment extends PropertyResolver {
    /**
     * 获取环境的配置文件
     *
     * @return
     */
    String[] getActiveProfiles();

    /**
     * 获取环境的默认配置文件
     *
     * @return
     */
    String[] getDefaultProfiles();

    boolean acceptsProfiles(String... profiles);
}
