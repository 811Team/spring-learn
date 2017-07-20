package team811.util;

import team811.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Description:1个key对应多个value
 * Created by 2017-07-07  11:02.
 * author: Zhou mingxiang
 */
public interface MultiValueMap<K,V> extends Map<K, List<V>> {
    /**
     * 返回给定键的第一个值
     *
     * @param key   给定键
     * @return
     */
    @Nullable
    V getFirst(K key);


    /**
     * 将给定的单个值添加到给定键值的当前值列表中
     *
     * @param key    给定键
     * @param value  给定的单个值
     */
    void add(K key, @Nullable V value);



    /**
     * 将给定list集合的所有值添加到给定键的当前值列表中。
     *
     * @param key     给定键
     * @param values  给定list集合
     */
    void addAll(K key, List<? extends V> values);


    /**
     *将给LinkedMultiValueMap添加到另一个LinkedMultiValueMap中，其中key值相同的合并value值
     *
     * @param values
     */
    void addAll(MultiValueMap<K, V> values);



    /**
     * 在给定的键下设置给定的单个值。
     *
     * @param key    键
     * @param value  值
     */
    void set(K key, V value);


    /**
     * 设置给定的值。
     *
     * @param values 值
     */
    void setAll(Map<K, V> values);


    /**
     * 返回此中包含的第一个值MultiValueMap。
     *
     * @return Map
     */
    Map<K, V> toSingleValueMap();
}
