package org.lucas.core;

import org.lucas.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @create: 2018-01-28
 * @description:
 */
public class SerializableTypeWrapper {

    interface TypeProvider extends Serializable {

        @Nullable
        Type getType();

        @Nullable
        default Object getSource() {
            return null;
        }
    }
}
