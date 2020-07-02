package org.lucas.aop.framework.autoproxy.target;

import org.lucas.beans.factory.BeanFactoryAware;

public abstract class AbstractBeanFactoryAwareAdvisingPostProcessor extends AbstractAdvisingBeanPostProcessor
        implements BeanFactoryAware {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (beanFactory instanceof ConfigurableListableBeanFactory ?
                (ConfigurableListableBeanFactory) beanFactory : null);
    }

}
