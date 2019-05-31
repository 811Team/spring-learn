package org.lucas;

import org.junit.Test;
import org.lucas.util.Util;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;
import sun.misc.Unsafe;

/**
 * @Author: shaw
 * @Date: 2019/5/31 18:34
 */
public class RingBufferTests {

    private static final Unsafe UNSAFE = Util.getUnsafe();

    @Test
    public void refArrayBase_FieldTest() {
        final int scale = UNSAFE.arrayIndexScale(Object[].class);
        final int elementShift = 2;
        final int offset = UNSAFE.arrayBaseOffset(Object[].class);
        final int bufferPad = 128 / scale;
        System.out.println("scale:" + scale);
        System.out.println("offset:" + offset);
        System.out.println("bufferPad:" + bufferPad);
        System.out.println(bufferPad << elementShift);
        final ClassLayout layout = ClassLayout.parseClass(Object[].class);
        printLayoutInfo(layout);
    }

    private static void printLayoutInfo(final ClassLayout layout) {
        System.out.println(VM.current().details());
        System.out.println("Printable:\n" + layout.toPrintable());
        System.out.println("headerSize: " + layout.headerSize());
        System.out.println("fields: " + layout.fields());
        System.out.println("instanceSize: " + layout.instanceSize());
    }
}
