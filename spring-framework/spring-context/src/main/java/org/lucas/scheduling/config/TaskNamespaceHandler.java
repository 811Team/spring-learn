package org.lucas.scheduling.config;

import org.lucas.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 负责标签<XXX:/>的处理
 */
public class TaskNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        // annotation-driven 对应 AnnotationDrivenBeanDefinitionParser 解析器
        this.registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
        this.registerBeanDefinitionParser("executor", new ExecutorBeanDefinitionParser());
        this.registerBeanDefinitionParser("scheduled-tasks", new ScheduledTasksBeanDefinitionParser());
        this.registerBeanDefinitionParser("scheduler", new SchedulerBeanDefinitionParser());
    }

}
