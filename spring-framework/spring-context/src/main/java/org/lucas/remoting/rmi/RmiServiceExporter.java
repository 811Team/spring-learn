package org.lucas.remoting.rmi;

import org.lucas.beans.factory.DisposableBean;
import org.lucas.beans.factory.InitializingBean;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

/**
 * @create: 2017-11-27
 * @description:
 */
public class RmiServiceExporter extends RmiBasedExporter implements InitializingBean, DisposableBean {

    /** 服务名 */
    private String serviceName;

    /** Socket客户端 */
    private RMIClientSocketFactory clientSocketFactory;

    /** Socket服务端 */
    private RMIServerSocketFactory serverSocketFactory;

    /**
     * 对象初始化
     *
     * @throws RemoteException
     * @see #prepare()
     */
    @Override
    public void afterPropertiesSet() throws RemoteException {
        prepare();
    }

    public void prepare() throws RemoteException {
        /**
         * 检查服务和服务名是否为空
         */
        checkService();
        if (this.serviceName == null) {
            throw new IllegalArgumentException("Property 'serviceName' is required");
        }

        if (this.clientSocketFactory instanceof RMIServerSocketFactory) {
            this.serverSocketFactory = (RMIServerSocketFactory) this.clientSocketFactory;
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    @Override
    public void destroy() throws Exception {

    }
}
