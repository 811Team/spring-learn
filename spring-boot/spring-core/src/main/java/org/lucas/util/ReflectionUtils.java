package org.lucas.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Description:用于处理反射API和处理的简单实用程序类
 * Created by 2017-07-06  14:30.
 * author: Zhou mingxiang
 */
public abstract class ReflectionUtils {

    /**
     * 为给定的类和参数获取一个可访问构造函数
     *
     * @param clazz          类
     * @param parameterTypes 构造函数的参数类型
     * @param <T>
     * @return 构造函数
     * @throws NoSuchMethodException
     */
    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {

        //获取给定参数类型的成员变量
        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);

        //设置对应构造函数访问权限
        makeAccessible(ctor);

        return ctor;
    }

    /**
     * 设置对应构造函数访问权限
     *
     *
     * @param ctor 构造函数
     */
    public static void makeAccessible(Constructor<?> ctor) {

        //ctor.getModifiers():返回此类或接口以整数编码的 Java语言修饰符(返回一个int型的返回值，代表类、成员变量、方法的修饰符)
        //Modifier.isPublic: 判断该整数对应的是不是包含public修饰符
        //ctor.isAccessible(): 获得构造函数的 accessible 标志的值


        //如果构造函数的修饰符不为public或者构造函数的声明类修饰符不为public或者构造函数的 accessible 标志值不存在
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {

            //将此构造函数(对象)的可访问标志设置为指示的布尔值。值为true表示反射的对象应该在使用时抑制Java语言访问检查。值为false表示反射对象应强制实施Java语言访问检查
            ctor.setAccessible(true);
        }
    }
}
