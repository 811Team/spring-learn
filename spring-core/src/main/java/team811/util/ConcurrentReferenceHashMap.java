package team811.util;

import team811.lang.Nullable;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @create: 2018-03-22
 * @description:
 */
public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    private static final int MAXIMUM_CONCURRENCY_LEVEL = 1 << 16;

    /** (1073741824) */
    private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;

    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;

    private final float loadFactor;

    private final int shift;

    private final Segment[] segments;

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

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
        Assert.isTrue(initialCapacity >= 0, "初始化容量不能为负数!");
        Assert.isTrue(loadFactor > 0f, "加载必须为正数!");
        Assert.isTrue(concurrencyLevel > 0, "并发计量必须为正数!");
        Assert.notNull(referenceType, "ReferenceType 不能为null");
        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, MAXIMUM_CONCURRENCY_LEVEL);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int) ((initialCapacity + size - 1L) / size);
        this.segments = (Segment[]) Array.newInstance(Segment.class, size);
        for (int i = 0, length = this.segments.length; i < length; i++) {
            this.segments[i] = new Segment(roundedUpSegmentCapacity);
        }
    }

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

    protected ReferenceManager createReferenceManager() {
        return new ReferenceManager();
    }

    protected class ReferenceManager {

    }

    protected interface Reference<K, V> {

        @Nullable
        Entry<K, V> get();

        int getHash();

        @Nullable
        Reference<K, V> getNext();

        void release();
    }

    public enum ReferenceType {

        /** Use {@link SoftReference}s */
        SOFT,

        /** Use {@link WeakReference}s */
        WEAK
    }

}
