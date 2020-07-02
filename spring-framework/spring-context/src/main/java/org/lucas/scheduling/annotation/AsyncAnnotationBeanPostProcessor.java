package org.lucas.scheduling.annotation;

import org.lucas.aop.framework.autoproxy.target.AbstractBeanFactoryAwareAdvisingPostProcessor;

/**
 * 异步注解 @Async 处理器
 */
public class AsyncAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);

        AsyncAnnotationAdvisor advisor = new AsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
        if (this.asyncAnnotationType != null) {
            advisor.setAsyncAnnotationType(this.asyncAnnotationType);
        }
        advisor.setBeanFactory(beanFactory);
        // 保存切面逻辑
        this.advisor = advisor;
    }

}
