package org.lucas.core;

import org.lucas.lang.Nullable;
import org.lucas.util.Assert;
import org.lucas.util.LinkedMultiValueMap;
import org.lucas.util.MultiValueMap;
import org.lucas.util.ReflectionUtils;

import java.util.*;

/**
 * Description:公共抽象类CollectionFactory扩展对象
 * 集合工厂，了解Java 5和Java 6集合。主要用于框架内部使用。
 * 此类的目标是避免运行时对特定Java版本的依赖，同时仍然使用运行时可用的最佳集合实现。
 * Created by 2017-07-06  11:07.
 * author: Zhou mingxiang
 */
public abstract class CollectionFactory {
    private static final Set<Class<?>> approximableCollectionTypes = new HashSet<Class<?>>();

    private static final Set<Class<?>> approximableMapTypes = new HashSet<Class<?>>();

    static {
        //将基本类型放入approximableCollectionTypes;
        approximableCollectionTypes.add(Collection.class);
        approximableCollectionTypes.add(List.class);
        approximableCollectionTypes.add(Set.class);
        approximableCollectionTypes.add(SortedSet.class);
        approximableCollectionTypes.add(NavigableSet.class);

        //将基本类型放入approximableMapTypes;
        approximableMapTypes.add(Map.class);
        approximableMapTypes.add(SortedMap.class);
        approximableMapTypes.add(NavigableMap.class);


        //将基本类型放入approximableCollectionTypes;
        approximableCollectionTypes.add(ArrayList.class);
        approximableCollectionTypes.add(LinkedList.class);
        approximableCollectionTypes.add(HashSet.class);
        approximableCollectionTypes.add(LinkedHashSet.class);
        approximableCollectionTypes.add(TreeSet.class);
        approximableCollectionTypes.add(EnumSet.class);

        //将基本类型放入approximableMapTypes;
        approximableMapTypes.add(HashMap.class);
        approximableMapTypes.add(LinkedHashMap.class);
        approximableMapTypes.add(TreeMap.class);
        approximableMapTypes.add(EnumMap.class);
    }

    /**
     * 判断集合类型是否存在approximableCollectionTypes集合中
     *
     * @param collectionType 集合类型
     * @return true --集合类型不为空并且approximableCollectionTypes集合包含对应集合类型
     * false -- 集合类型为空 或者不存在approximableCollectionTypes集合中
     */
    public static boolean isApproximableCollectionType(@Nullable Class<?> collectionType) {
        //集合类型不为空并且approximableCollectionTypes集合包含对应集合类型
        return (collectionType != null && approximableCollectionTypes.contains(collectionType));
    }

    /**
     * 为给定集合创建新的集合（分别为LinkedList，SortedSet或EnumSet创建LinkedList，ArrayList，TreeSet或EnumSet）
     *
     * @param collection 原始Collection对象
     * @param capacity   初始容量
     * @param <E>
     * @return 新的Collection实例
     */
    public static <E> Collection<E> createApproximateCollection(@Nullable Object collection, int capacity) {

        //判断collection是否是LinkedList或者LinkedList子类的实例
        if (collection instanceof LinkedList) {
            return new LinkedList<E>();
            //判断collection是否是List或者List子类的实例
        } else if (collection instanceof List) {
            return new ArrayList<E>(capacity);
            //判断collection是否是EnumSet或者EnumSet子类的实例
        } else if (collection instanceof EnumSet) {

            //克隆collectiond的EnumSet转换值，在强转为 Collection
            Collection<E> enumSet = (Collection<E>) EnumSet.copyOf((EnumSet) collection);

            //清理集合内容
            enumSet.clear();

            return enumSet;
        } else if (collection instanceof SortedSet) {
            //返回TreeSet， ((SortedSet<E>) collection).comparator()定义比较器中比较的数据类型必须是SortedSet<E> 或者SortedSet<E> 的超类
            return new TreeSet<E>(((SortedSet<E>) collection).comparator());
        } else {
            return new LinkedHashSet<E>(capacity);
        }
    }

    /**
     * 为给定的集合类型创建最合适的集合
     *
     * @param collectionType 目标集合的所属类型
     * @param capacity       初始容量
     * @param <E>
     * @return 一个新的Collection集合实例
     */
    public static <E> Collection<E> createCollection(Class<?> collectionType, int capacity) {
        return createCollection(collectionType, null, capacity);
    }

    /**
     * 为给定的集合类型创建最合适的集合
     *
     * @param collectionType 目标集合的所需类型
     * @param elementType    集合的元素类型
     * @param capacity       初始容量
     * @param <E>
     * @return 一个新的Collection集合实例
     */
    public static <E> Collection<E> createCollection(Class<?> collectionType, @Nullable Class<?> elementType, int capacity) {
        Assert.notNull(collectionType, "Collection type must not be null");

        //目标集合的所需类型是否是接口
        if (collectionType.isInterface()) {
            if (Set.class == collectionType || Collection.class == collectionType) {
                return new LinkedHashSet<E>(capacity);
            } else if (List.class == collectionType) {
                return new ArrayList<E>(capacity);
            } else if (SortedSet.class == collectionType || NavigableSet.class == collectionType) {
                return new TreeSet<E>();
            } else {
                throw new IllegalArgumentException("Unsupported Collection interface: " + collectionType.getName());
            }
        } else if (EnumSet.class == collectionType) {
            Assert.notNull(elementType, "Cannot create EnumSet for unknown element type");
            // Cast is necessary for compilation in Eclipse 4.4.1.
            return (Collection<E>) EnumSet.noneOf(asEnumType(elementType));
        } else {
            //判断Collection和collectionType是否相同或是collectionType的超类或接口，否则抛出异常
            if (!Collection.class.isAssignableFrom(collectionType)) {
                //非法参数
                throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
            }
            try {
                return (Collection<E>) ReflectionUtils.accessibleConstructor(collectionType).newInstance();
            } catch (Throwable ex) {
                throw new IllegalArgumentException(
                        "Could not instantiate Collection type: " + collectionType.getName(), ex);
            }
        }
    }

    /**
     * 判断集合类型是否存在approximableMapTypes集合中
     *
     * @param mapType 集合类型
     * @return true --集合类型不为空并且approximableMapTypes集合包含对应集合类型
     * false -- 集合类型为空 或者不存在approximableMapTypes集合中
     */
    public static boolean isApproximableMapType(@Nullable Class<?> mapType) {
        return (mapType != null && approximableMapTypes.contains(mapType));
    }

    /**
     * 为给定的集合类型创建最合适的集合
     * 分别为SortedMap或Map创建TreeMap或LinkedHashMap。
     *
     * @param map      原始的Map对象
     * @param capacity 初始容量
     * @param <K>
     * @param <V>
     * @return 新的Map实例
     */
    public static <K, V> Map<K, V> createApproximateMap(@Nullable Object map, int capacity) {

        //判断原始的Map对象是否是EnumMapt或者EnumMap子类的实例
        if (map instanceof EnumMap) {
            EnumMap enumMap = new EnumMap((EnumMap) map);
            enumMap.clear();
            return enumMap;
        } else if (map instanceof SortedMap) {  //判断原始的Map对象是否是SortedMap或者SortedMap子类的实例
            return new TreeMap<K, V>(((SortedMap<K, V>) map).comparator());
        } else {
            return new LinkedHashMap<K, V>(capacity);
        }
    }

    /**
     * 为给定的集合类型创建最合适的集合
     *
     * @param mapType  目标集合的所属类型
     * @param capacity 初始容量
     * @param <K>
     * @param <V>
     * @return 新的Map实例
     */
    public static <K, V> Map<K, V> createMap(Class<?> mapType, int capacity) {
        return createMap(mapType, null, capacity);
    }

    /**
     * 为给定的集合类型创建最合适的集合
     *
     * @param mapType  目标集合的所属类型
     * @param keyType  集合的元素类型
     * @param capacity 初始容量
     * @param <K>
     * @param <V>
     * @return 新的Map实例
     */
    public static <K, V> Map<K, V> createMap(Class<?> mapType, @Nullable Class<?> keyType, int capacity) {
        Assert.notNull(mapType, "Map type must not be null");
        if (mapType.isInterface()) {
            if (Map.class == mapType) {
                return new LinkedHashMap<K, V>(capacity);
            } else if (SortedMap.class == mapType || NavigableMap.class == mapType) {
                return new TreeMap<K, V>();
            } else if (MultiValueMap.class == mapType) {
                return new LinkedMultiValueMap();
            } else {
                throw new IllegalArgumentException("Unsupported Map interface: " + mapType.getName());
            }
        } else if (EnumMap.class == mapType) {
            Assert.notNull(keyType, "Cannot create EnumMap for unknown key type");
            return new EnumMap(asEnumType(keyType));
        } else {
            if (!Map.class.isAssignableFrom(mapType)) {
                throw new IllegalArgumentException("Unsupported Map type: " + mapType.getName());
            }
            try {
                return (Map<K, V>) ReflectionUtils.accessibleConstructor(mapType).newInstance();
            } catch (Throwable ex) {
                throw new IllegalArgumentException("Could not instantiate Map type: " + mapType.getName(), ex);
            }
        }
    }

    public static Properties createStringAdaptingProperties() {
        return new Properties() {
            @Override
            @Nullable
            public String getProperty(String key) {
                Object value = get(key);
                return (value != null ? value.toString() : null);
            }
        };
    }

    /**
     * 获取给定类（对象）的子类
     *
     * @param enumType 集合的元素类型
     * @return 返回给定类（对象）的子类
     */
    private static Class<? extends Enum> asEnumType(Class<?> enumType) {

        Assert.notNull(enumType, "Enum type must not be null");

        //Enum类和enumType是否相同或是enumType的子类或接口， 否 抛出异常
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("Supplied type is not an enum: " + enumType.getName());
        }

        //返回Enum类（对象）的子类
        return enumType.asSubclass(Enum.class);
    }

}
