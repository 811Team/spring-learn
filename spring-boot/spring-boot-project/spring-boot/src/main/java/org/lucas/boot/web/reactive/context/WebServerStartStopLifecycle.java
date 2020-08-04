package org.lucas.boot.web.reactive.context;

import org.springframework.context.SmartLifecycle;

/**
 * 通过实现 SmartLifecycle 接口，将 Web 服务器生命周期交有 spring 进行管理。
 */
class WebServerStartStopLifecycle implements SmartLifecycle {

    private volatile boolean running;

    private final WebServerManager weServerManager;

    WebServerStartStopLifecycle(WebServerManager weServerManager) {
        this.weServerManager = weServerManager;
    }

    /**
     * 组件在 finishRefresh 阶段启动
     */
    @Override
    public void start() {
        this.weServerManager.start();
        this.running = true;
    }

    /**
     * 组件停止
     */
    @Override
    public void stop() {
        this.running = false;
        this.weServerManager.stop();
    }

    /**
     * Spring 用于检查该组件当前是否正在运行，如果
     * 为 true,将不执行 start() 方法。
     *
     * @return 在容器的情况下，只有当应用的所有组件当前都在运行时，才会返回true。
     */
    @Override
    public boolean isRunning() {
        return this.running;
    }

    /**
     * 启动过程从最低值开始，关闭过程将应用相反的顺序。
     * 任何有相同的值将在相同的相位内任意排列。
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE - 1;
    }
}
