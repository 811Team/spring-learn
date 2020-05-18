package org.lucas.context.annotation;

import org.lucas.lang.Nullable;

import java.lang.annotation.Annotation;

public abstract class AdviceModeImportSelector <A extends Annotation> implements ImportSelector {

    @Nullable
    protected abstract String[] selectImports(AdviceMode adviceMode);

}
