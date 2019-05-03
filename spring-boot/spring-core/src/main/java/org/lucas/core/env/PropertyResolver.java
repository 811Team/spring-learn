package org.lucas.core.env;

import org.lucas.lang.Nullable;

/**
 * @create: 2018-01-26
 * @description:
 */
public interface PropertyResolver {

    /**
     * 是否包含指定 key 的值
     *
     * @param key {@code String}
     * @return {@code true} 表示存在
     */
    boolean containsProperty(String key);

    /**
     * 通过 key 获取值
     *
     * @param key {@code String}
     * @return value 值
     */
    @Nullable
    String getProperty(String key);

    /**
     * 通过 key 获取值，
     * 如果为空，则返回 defaultValue 的值
     *
     * @param key {@code String}
     * @return value 值
     */
    String getProperty(String key, String defaultValue);

    /**
     * 通过 key 获取类型为 targetType 的对象。
     *
     * @param key        {@code String} key
     * @param targetType {@code Class<T>} 类类型
     * @param <T>
     * @return targetType 对象
     */
    @Nullable
    <T> T getProperty(String key, Class<T> targetType);

    /**
     * 通过 key 获取类型为 targetType 的对象。
     * 如果为 null,则返回 defaultValue。
     *
     * @param key          {@code String} key
     * @param targetType   {@code Class<T>} 类类型
     * @param defaultValue {@code Class<T>} 默认返回类类型
     * @param <T>
     * @return
     */
    <T> T getProperty(String key, Class<T> targetType, T defaultValue);

    /**
     * 通过 key 获取值
     *
     * @param key {@code String} key
     * @return
     * @throws IllegalStateException 如果不能通过 key 获取值，则抛出 {@code IllegalStateException}
     */
    String getRequiredProperty(String key) throws IllegalStateException;

    /**
     * 通过 key 获取类型为 targetType 的对象。
     *
     * @param key        {@code String} key
     * @param targetType {@code Class<T>} 类类型
     * @param <T>
     * @return
     * @throws IllegalStateException 如果不能通过 key 获取对象，则抛出 {@code IllegalStateException}
     */
    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;

    /**
     *
     * @param text
     * @return
     */
    String resolvePlaceholders(String text);

    String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}
