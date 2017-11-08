package team811.scheduling.concurrent;

import team811.beans.factory.BeanNameAware;
import team811.beans.factory.DisposableBean;
import team811.beans.factory.InitializingBean;
import team811.lang.Nullable;

/**
 * @create: 2017-11-08
 * @description:
 */
public abstract class ExecutorConfigurationSupport extends CustomizableThreadFactory
        implements BeanNameAware, InitializingBean, DisposableBean {

    protected final Log logger = LogFactory.getLog(getClass());

    @Nullable
    private String beanName;

    @Override
    public void destroy() throws Exception {
        shutdown();
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void shutdown() {
        if (logger.isInfoEnabled()) {
            logger.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
        }
        if (this.executor != null) {
            if (this.waitForTasksToCompleteOnShutdown) {
                this.executor.shutdown();
            }
            else {
                this.executor.shutdownNow();
            }
            awaitTerminationIfNecessary(this.executor);
        }
    }

}
