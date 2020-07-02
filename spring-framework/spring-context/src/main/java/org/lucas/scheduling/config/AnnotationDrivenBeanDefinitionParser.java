package org.lucas.scheduling.config;

import org.lucas.beans.factory.xml.BeanDefinitionParser;
import org.lucas.lang.Nullable;
import org.w3c.dom.Element;

public class AnnotationDrivenBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * 创建 AsyncAnnotationBeanPostProcessor 在Spring容器中的元数据定义，
     * 并注册到Spring容器中，剩下的流程与基于 @EnableAsync 注解开启异步处理的流程一样。
     *
     * @param element
     * @param parserContext
     * @return
     */
    @Override
    @Nullable
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Object source = parserContext.extractSource(element);

        // Register component for the surrounding <task:annotation-driven> element.
        CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
        parserContext.pushContainingComponent(compDefinition);

        // Nest the concrete post-processor bean in the surrounding component.
        BeanDefinitionRegistry registry = parserContext.getRegistry();

        String mode = element.getAttribute("mode");
        if ("aspectj".equals(mode)) {
            // mode="aspectj"
            registerAsyncExecutionAspect(element, parserContext);
        } else {
            // mode="proxy"
            if (registry.containsBeanDefinition(TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME)) {
                parserContext.getReaderContext().error(
                        "Only one AsyncAnnotationBeanPostProcessor may exist within the context.", source);
            } else {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
                        "org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor");
                builder.getRawBeanDefinition().setSource(source);
                String executor = element.getAttribute("executor");
                if (StringUtils.hasText(executor)) {
                    builder.addPropertyReference("executor", executor);
                }
                String exceptionHandler = element.getAttribute("exception-handler");
                if (StringUtils.hasText(exceptionHandler)) {
                    builder.addPropertyReference("exceptionHandler", exceptionHandler);
                }
                if (Boolean.parseBoolean(element.getAttribute(AopNamespaceUtils.PROXY_TARGET_CLASS_ATTRIBUTE))) {
                    builder.addPropertyValue("proxyTargetClass", true);
                }
                registerPostProcessor(parserContext, builder, TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME);
            }
        }

        if (registry.containsBeanDefinition(TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            parserContext.getReaderContext().error(
                    "Only one ScheduledAnnotationBeanPostProcessor may exist within the context.", source);
        } else {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
                    "org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor");
            builder.getRawBeanDefinition().setSource(source);
            String scheduler = element.getAttribute("scheduler");
            if (StringUtils.hasText(scheduler)) {
                builder.addPropertyReference("scheduler", scheduler);
            }
            registerPostProcessor(parserContext, builder, TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME);
        }

        // Finally register the composite component.
        parserContext.popAndRegisterContainingComponent();

        return null;
    }

}
