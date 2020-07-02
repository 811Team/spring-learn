package org.lucas.aop.support;

import org.aopalliance.aop.Advice;
import org.lucas.aop.PointcutAdvisor;
import org.lucas.core.Ordered;
import org.lucas.lang.Nullable;

import java.io.Serializable;

public abstract class AbstractPointcutAdvisor implements PointcutAdvisor, Ordered, Serializable {

    @Nullable
    private Integer order;

    /**
     * 手动指定优先级
     *
     * @param order 优先级级别
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        if (this.order != null) {
            return this.order;
        }
        Advice advice = getAdvice();
        // 若调用者没有指定 Order，那就拿advice的order为准
        // 否则LOWEST_PRECEDENCE表示最后执行
        if (advice instanceof Ordered) {
            return ((Ordered) advice).getOrder();
        }
        return Ordered.LOWEST_PRECEDENCE;
    }

}
