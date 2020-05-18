package org.lucas.scheduling.annotation;

import org.lucas.context.annotation.AdviceMode;
import org.lucas.context.annotation.AdviceModeImportSelector;
import org.lucas.lang.Nullable;

public class AsyncConfigurationSelector extends AdviceModeImportSelector<EnableAsync> {

    private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME =
            "org.lucas.scheduling.aspectj.AspectJAsyncConfiguration";

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        // 根据 AdviceMode 参数返回需要导入到 Spring 容器的 Bean 的全路径包名。该方法会
        // 在 ConfigurationClassPostProcessor 中的 ConfigurationClassParser 类中
        // 调用。默认情况下的 adviceMode 为 PROXY，所以会把 ProxyAsyncConfiguration
        // 的实例注入 Spring 容器。
        switch (adviceMode) {
            case PROXY:
                return new String[]{ProxyAsyncConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME};
            default:
                return null;
        }
    }
}
