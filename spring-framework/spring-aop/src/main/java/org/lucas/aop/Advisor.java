package org.lucas.aop;

import org.aopalliance.aop.Advice;

public interface Advisor {

    /**
     * @return 切面逻辑
     */
    Advice getAdvice();

}
