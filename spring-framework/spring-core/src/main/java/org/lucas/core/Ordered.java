package org.lucas.core;

public interface Ordered {

    /**
     * 最高处理优先级值
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * 最低处理优先级值
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    int getOrder();

}
