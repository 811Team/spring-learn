package team811.remoting.support;

import team811.util.Assert;

/**
 * @create: 2017-11-27
 * @description:
 */
public abstract class RemoteExporter extends RemotingSupport {

    private Object service;

    /**
     * 判断服务是否为空
     *
     * @throws IllegalArgumentException
     */
    protected void checkService() throws IllegalArgumentException {
        Assert.notNull(getService(), "Property 'service' is required");
    }

    /**
     * 将服务返回
     *
     * @return
     */
    public Object getService() {
        return this.service;
    }
}
