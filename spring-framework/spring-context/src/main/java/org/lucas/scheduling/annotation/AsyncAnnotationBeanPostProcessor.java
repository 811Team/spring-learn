package org.lucas.scheduling.annotation;

import org.lucas.aop.Advisor;
import org.lucas.aop.framework.autoproxy.target.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.lucas.lang.Nullable;

public class AsyncAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    /**
     * 切面
     */
    @Nullable
    protected Advisor advisor;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);

        AsyncAnnotationAdvisor advisor = new AsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
        if (this.asyncAnnotationType != null) {
            advisor.setAsyncAnnotationType(this.asyncAnnotationType);
        }
        advisor.setBeanFactory(beanFactory);
        // 保存切面
        this.advisor = advisor;
    }

}
