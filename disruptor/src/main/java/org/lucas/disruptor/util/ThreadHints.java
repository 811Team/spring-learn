package org.lucas.disruptor.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;

public final class ThreadHints {

    private static final MethodHandle ON_SPIN_WAIT_METHOD_HANDLE;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle = null;
        try {
            methodHandle = lookup.findStatic(Thread.class, "onSpinWait", methodType(void.class));
        } catch (final Exception ignore) {
        }
        ON_SPIN_WAIT_METHOD_HANDLE = methodHandle;
    }

    private ThreadHints() {
    }

    /**
     * 表示调用方暂时无法进行处理，直到其他活动中出现一个或多个操作。
     * 通过在spin-wait循环构造的每个迭代中调用此方法，调用线程向运行时表明它正在忙等待。
     * 运行时可以采取行动来改进调用自旋等待循环结构的性能。
     */
    public static void onSpinWait() {
        if (null != ON_SPIN_WAIT_METHOD_HANDLE) {
            try {
                ON_SPIN_WAIT_METHOD_HANDLE.invokeExact();
            } catch (final Throwable ignore) {
            }
        }
    }

}
