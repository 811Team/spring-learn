package org.lucas.util;

import org.junit.Test;

/**
 * @create: 2017-11-10
 * @description:
 */
public class ClassUtilsTests {

    @Test
    public void getShortNameTest() {
        System.out.println(ClassUtils.getShortName(ClassUtils.class));
    }
}
