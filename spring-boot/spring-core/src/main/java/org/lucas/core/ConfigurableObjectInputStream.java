package org.lucas.core;

import org.lucas.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;

/**
 * @create: 2017-06-30
 * @description: 继承:ObjectInputStream反序列化对象
 */
public class ConfigurableObjectInputStream extends ObjectInputStream {

    /**
     * 类加载器
     */
    private final ClassLoader classLoader;

    private final boolean acceptProxyClasses;

    /**
     * @param in          输入流对象
     * @param classLoader 类加载器(初始化后只读)
     * @throws IOException
     * @description: 构造方法
     */
    public ConfigurableObjectInputStream(InputStream in, @Nullable ClassLoader classLoader) throws IOException {
        this(in, classLoader, true);
    }

    /**
     * @param in                 输入流对象
     * @param classLoader        类加载器(初始化后只读)
     * @param acceptProxyClasses
     * @throws IOException
     * @description: 构造方法
     */
    public ConfigurableObjectInputStream(InputStream in, @Nullable ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {
        //调用父类构造方法
        super(in);
        this.classLoader = classLoader;
        this.acceptProxyClasses = acceptProxyClasses;
    }

    @Override
    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        if (!this.acceptProxyClasses) {
            throw new NotSerializableException("Not allowed to accept serialized proxy classes");
        }
        if (this.classLoader != null) {
            // 类类型数组对象,长度为字符串数组长度
            Class<?>[] resolvedInterfaces = new Class<?>[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                // resolvedInterfaces[i] = ClassUtils.forName(interfaces[i], this.classLoader);
            }
        }
        return null;
    }
}
