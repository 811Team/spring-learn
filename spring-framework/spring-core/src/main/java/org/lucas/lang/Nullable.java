package org.lucas.lang;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @create: 2017-06-30
 * @description:
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})  //目标:方法,参数,属性名
@Retention(RetentionPolicy.RUNTIME)  //运行时可见
@Nonnull(when = When.MAYBE)  //...
@TypeQualifierNickname  //...
public @interface Nullable {
}
