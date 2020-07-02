package org.lucas.context.annotation;

import java.util.Set;

/**
 * 分析一个 @Configuration 注解的类
 */
class ConfigurationClassParser {

    /**
     * 针对每个候选配置类元素BeanDefinitionHolder，执行以下逻辑 :
     * 1.将其封装成一个ConfigurationClass
     * 2.调用processConfigurationClass(ConfigurationClass configClass)
     * 分析过的每个配置类都被保存到属性 this.configurationClasses 中。
     *
     * @param configCandidates
     */
    public void parse(Set<BeanDefinitionHolder> configCandidates) {
        this.deferredImportSelectors = new LinkedList<DeferredImportSelectorHolder>();

        for (BeanDefinitionHolder holder : configCandidates) {
            BeanDefinition bd = holder.getBeanDefinition();
            try {
                // 这里根据Bean定义的不同类型走不同的分支，但是最终都会调用到方法
                //  processConfigurationClass(ConfigurationClass configClass)
                if (bd instanceof AnnotatedBeanDefinition) {
                    // bd 是一个 AnnotatedBeanDefinition
                    parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
                } else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
                    // bd 是一个 AbstractBeanDefinition,并且指定 beanClass 属性
                    parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
                } else {
                    // 其他情况
                    parse(bd.getBeanClassName(), holder.getBeanName());
                }
            } catch (BeanDefinitionStoreException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new BeanDefinitionStoreException(
                        "Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
            }
        }

        // 执行找到的 DeferredImportSelector
        //  DeferredImportSelector 是 ImportSelector 的一个变种。
        // ImportSelector 被设计成其实和@Import注解的类同样的导入效果，但是实现 ImportSelector
        // 的类可以条件性地决定导入哪些配置。
        // DeferredImportSelector 的设计目的是在所有其他的配置类被处理后才处理。这也正是
        // 该语句被放到本函数最后一行的原因。
        processDeferredImportSelectors();
    }

}
