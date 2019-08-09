package org.lucas.classify;

import java.io.Serializable;

public interface Classifier<C, T> extends Serializable {

    /**
     * 通过类型进行匹配类型
     *
     * @param classifiable 匹配类型
     * @return 被匹配到的类型
     */
    T classify(C classifiable);

}
