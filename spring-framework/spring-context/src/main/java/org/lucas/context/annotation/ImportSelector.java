package org.lucas.context.annotation;

import org.lucas.core.type.AnnotationMetadata;
import org.lucas.lang.Nullable;

import java.util.function.Predicate;

public interface ImportSelector {

    String[] selectImports(AnnotationMetadata importingClassMetadata);

    @Nullable
    default Predicate<String> getExclusionFilter() {
        return null;
    }

}
