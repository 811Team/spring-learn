package team811.lang;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;
import java.lang.annotation.*;

/**
 * @create: 2017-06-30
 * @description:
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})  //目标:方法,参数
@Retention(RetentionPolicy.RUNTIME)  //运行时可见
@Nonnull(when = When.MAYBE)  //...
@TypeQualifierNickname  //...
public @interface Nullable {
}
