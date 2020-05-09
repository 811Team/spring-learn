package org.lucas.util;

import org.lucas.lang.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @create: 2018-03-22
 * @description:
 */
public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    /**
     * 默认容量
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * 默认负载因子
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 默认最小预设长度
     */
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    /**
     * 默认最大预设长度（32）
     */
    private static final int MAXIMUM_CONCURRENCY_LEVEL = 1 << 16;

    /**
     * (1073741824)
     */
    private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;

    /**
     * 默认引用类型
     * 1. ReferenceType.SOFT： 软引用，有用但并非必需的对象。软引用关联的对象，
     * 在系统发生内存溢出异常之前，将会把这些对象列进回收范围之中进行第二次回收。
     * <p>
     * 2. ReferenceType.WEAK： 弱引用，非必需的对象。弱引用关联的对象只能生存到
     * 下一次垃圾收集发生之前，当垃圾收集器工作时，无论当前内存是否足够，都将回收掉只被弱引用关联的对象。
     */
    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;

    /**
     * 负荷因子
     */
    private final float loadFactor;

    /**
     * 位移值
     */
    private final int shift;

    /**
     * 分段
     */
    private final Segment[] segments;

    /**
     * 引用类型
     */
    private final ReferenceType referenceType;

    public ConcurrentReferenceHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, referenceType);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    /**
     * @param initialCapacity  初始化容量
     * @param loadFactor       负载因子
     * @param concurrencyLevel 预设长度
     * @param referenceType    引用类型
     */
    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
        Assert.isTrue(initialCapacity >= 0, "初始化容量不能为负数!");
        Assert.isTrue(loadFactor > 0f, "加载必须为正数!");
        Assert.isTrue(concurrencyLevel > 0, "预设长度必须为正数!");
        Assert.notNull(referenceType, "Reference type must not be null");
        this.loadFactor = loadFactor;
        // 计算位移值
        this.shift = calculateShift(concurrencyLevel, MAXIMUM_CONCURRENCY_LEVEL);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int) ((initialCapacity + size - 1L) / size);
        this.segments = (Segment[]) Array.newInstance(Segment.class, size);
        for (int i = 0, length = this.segments.length; i < length; i++) {
            this.segments[i] = new Segment(roundedUpSegmentCapacity);
        }
    }

    /**
     * 计算 {@link #shift} 位移值
     *
     * @param minimumValue 最小预设值
     * @param maximumValue 最大预设值
     * @return 位移值
     */
    protected static int calculateShift(int minimumValue, int maximumValue) {
        int shift = 0;
        int value = 1;
        while (value < minimumValue && value < maximumValue) {
            // value * 2
            value <<= 1;
            shift++;
        }
        return shift;
    }

    /**
     * @return 负载因子
     */
    protected final float getLoadFactor() {
        return this.loadFactor;
    }

    protected final int getSegmentsSize() {
        return this.segments.length;
    }

    protected final Segment getSegment(int index) {
        return this.segments[index];
    }

    protected ReferenceManager createReferenceManager() {
        return new ReferenceManager();
    }

    /**
     * 使用Wang/Jenkins算法计算对象的hash值，缺点是相对其它算法较慢。
     * 特性是：
     * 1.雪崩性（更改输入参数的任何一位，就将引起输出有一半以上的位发生变化）
     * 2.可逆性
     *
     * @param o 计算对象
     * @return 哈希值
     */
    protected int getHash(@Nullable Object o) {
        int hash = (o != null ? o.hashCode() : 0);
        hash += (hash << 15) ^ 0xffffcd7d;
        hash ^= (hash >>> 10);
        hash += (hash << 3);
        hash ^= (hash >>> 6);
        hash += (hash << 2) + (hash << 14);
        hash ^= (hash >>> 16);
        return hash;
    }

    @Override
    @Nullable
    public V get(@Nullable Object key) {
        Entry<K, V> entry = getEntryIfAvailable(key);
    }

    @Nullable
    private Entry<K, V> getEntryIfAvailable(@Nullable Object key) {
        Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
    }

    protected final class Segment extends ReentrantLock {

        private final int initialSize;

        private final ReferenceManager referenceManager;

        private volatile Reference<K, V>[] references;

        public Segment(int initialCapacity) {
            this.referenceManager = createReferenceManager();
            this.initialSize = 1 << calculateShift(initialCapacity, MAXIMUM_SEGMENT_SIZE);
            this.references = createReferenceArray(this.initialSize);
        }

        @SuppressWarnings("unchecked")
        private Reference<K, V>[] createReferenceArray(int size) {
            return (Reference<K, V>[]) Array.newInstance(Reference.class, size);
        }

    }

    protected class ReferenceManager {

        public Reference<K, V> createReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next) {
            if (ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK) {
                return new WeakEntryReference<>(entry, hash, next, this.queue);
            }
        }

    }

    protected interface Reference<K, V> {

        @Nullable
        Entry<K, V> get();

        int getHash();

        @Nullable
        Reference<K, V> getNext();

        void release();
    }

    protected static final class Entry<K, V> implements Map.Entry<K, V> {

        @Nullable
        private final K key;

        @Nullable
        private volatile V value;

        public Entry(@Nullable K key, @Nullable V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        @Nullable
        public K getKey() {
            return this.key;
        }

        @Override
        @Nullable
        public V getValue() {
            return this.value;
        }

        /**
         * 设置 {@link #value} 返回上一个 {@link #value}
         *
         * @param value 值
         * @return 上一个 {@link #value}
         */
        @Override
        @Nullable
        public V setValue(@Nullable V value) {
            V previous = this.value;
            this.value = value;
            return previous;
        }

        @Override
        public String toString() {
            return (this.key + "=" + this.value);
        }

        @Override
        public final boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            // 如果不是 Map.Entry 子类直接返回
            if (!(other instanceof Map.Entry)) {
                return false;
            }
            // 比较键值对
            Map.Entry otherEntry = (Map.Entry) other;
            return (ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) &&
                    ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue()));
        }

        @Override
        public final int hashCode() {
            return (ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value));
        }

    }

    private static final class WeakEntryReference<K, V> extends WeakReference<Entry<K, V>> implements Reference<K, V> {

        private final int hash;

        @Nullable
        private final Reference<K, V> nextReference;

        public WeakEntryReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next,
                                  ReferenceQueue<Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        @Override
        public void release() {
            enqueue();
            clear();
        }
    }

    public enum ReferenceType {

        /**
         * Use {@link SoftReference}s
         */
        SOFT,

        /**
         * Use {@link WeakReference}s
         */
        WEAK
    }

}
