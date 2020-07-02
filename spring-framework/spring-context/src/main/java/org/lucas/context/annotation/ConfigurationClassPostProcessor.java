package org.lucas.context.annotation;

import org.lucas.beans.factory.BeanClassLoaderAware;
import org.lucas.context.ResourceLoaderAware;
import org.lucas.core.PriorityOrdered;

/**
 * 用于解析注解类，并把其注册到 spring 容器中。
 * 其可以解析标注 @Configuration、@Component、@ComponentScan、@Import、@ImportResource 等的 Bean。
 * <p>
 * 使用 <context:annotation-config/> 或 <context:component-scan/> 时默认处理，否则需要手动声明相关注解
 * <p>
 * 这个处理器是优先的，因为在 @Configuration 类中声明的任何 Bean 方法都必须在
 * 任何其他 BeanFactoryPostProcessor 执行之前注册它们对应的 Bean 定义.
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor,
        PriorityOrdered, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {



}
