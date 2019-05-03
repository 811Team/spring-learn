package org.lucas.lang;

/**
 * @create: 2017-07-03
 * @description: 理解 @Nullable 用途
 */
public class NullableTest {

    public static void nullableCase(@Nullable String arg){

    }

    public static void main(String[] args) {
        nullableCase(null);
    }
}
