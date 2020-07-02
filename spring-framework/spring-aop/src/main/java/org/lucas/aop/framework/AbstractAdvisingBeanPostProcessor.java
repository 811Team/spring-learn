package org.lucas.aop.framework;

import org.lucas.aop.Advisor;
import org.lucas.aop.support.AopUtils;
import org.lucas.beans.factory.config.BeanPostProcessor;
import org.lucas.lang.Nullable;

public abstract class AbstractAdvisingBeanPostProcessor extends ProxyProcessorSupport implements BeanPostProcessor {

    /**
     * 切面逻辑
     */
    @Nullable
    protected Advisor advisor;

    /**
     * 当 bean 加载完后，通过切面增强目标业务逻辑，生成代理类
     *
     * @param bean     bean 对象
     * @param beanName bean 名称
     * @return 增强代理对象
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (this.advisor == null || bean instanceof AopInfrastructureBean) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }

        if (bean instanceof Advised) {
            Advised advised = (Advised) bean;
            if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
                // Add our local Advisor to the existing proxy's Advisor chain...
                if (this.beforeExistingAdvisors) {
                    advised.addAdvisor(0, this.advisor);
                } else {
                    advised.addAdvisor(this.advisor);
                }
                return bean;
            }
        }

        if (isEligible(bean, beanName)) {
            // 2.1 创建代理工厂
            ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
            if (!proxyFactory.isProxyTargetClass()) {
                evaluateProxyInterfaces(bean.getClass(), proxyFactory);
            }
            // 2.2 将代理工厂的切面设置为 advisor
            proxyFactory.addAdvisor(this.advisor);
            customizeProxyFactory(proxyFactory);
            // 2.3 生成代理类，并返回到 spring 容器中
            // 代理类内部会先使用 advisor 中的 PointCut 进行比较，
            // 看其是否符合切点条件，如果不符合则直接调用被代理的对象的原生方法。
            // 否则调用 advisor 中的 Advice 进行拦截处理。
            return proxyFactory.getProxy(getProxyClassLoader());
        }
        // No proxy needed.
        return bean;
    }

    /**
     * 创建代理工厂
     *
     * @param bean
     * @param beanName
     * @return
     */
    protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.copyFrom(this);
        proxyFactory.setTarget(bean);
        return proxyFactory;
    }
}
