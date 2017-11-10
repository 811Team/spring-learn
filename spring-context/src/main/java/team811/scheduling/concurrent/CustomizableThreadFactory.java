package team811.scheduling.concurrent;

import team811.util.CustomizableThreadCreator;

import java.util.concurrent.ThreadFactory;

/**
 * 作用:
 * 1.创建新线程
 */
public class CustomizableThreadFactory extends CustomizableThreadCreator
        implements ThreadFactory {

    /**
     * 线程创建处理的(调用父类方法)
     *
     * @param r Runnable 对象
     * @return Thread
     */
    @Override
    public Thread newThread(Runnable r) {
        return createThread(r);
    }

}
