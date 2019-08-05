package org.lucas.classify;

import java.io.Serializable;

public interface Classifier<C, T> extends Serializable {

    T classify(C classifiable);

}
