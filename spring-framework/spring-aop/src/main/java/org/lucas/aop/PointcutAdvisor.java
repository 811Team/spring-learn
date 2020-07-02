package org.lucas.aop;

public interface PointcutAdvisor extends Advisor {

    /**
     * @return 切点
     */
    Pointcut getPointcut();

}
