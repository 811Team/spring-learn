package org.lucas.classify;

import java.io.Serializable;

public interface Classifier<C, T> extends Serializable {

    /**
     * 通过C类型进行匹配T值（包含接口，父类）
     *
     * @param classifiable 匹配类型
     * @return 被匹配到的类型
     */
    T classify(C classifiable);

}
