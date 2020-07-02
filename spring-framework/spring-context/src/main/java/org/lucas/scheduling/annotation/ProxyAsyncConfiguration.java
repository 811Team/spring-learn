package org.lucas.scheduling.annotation;

import org.lucas.context.annotation.Bean;
import org.lucas.scheduling.config.TaskManagementConfigUtils;

public class ProxyAsyncConfiguration extends AbstractAsyncConfiguration {

    /**
     * 该方法返回的 AsyncAnnotationBeanPostProcessor 会注入到 spring 中。
     */
    @Bean(name = TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public AsyncAnnotationBeanPostProcessor asyncAdvisor() {

    }

}
