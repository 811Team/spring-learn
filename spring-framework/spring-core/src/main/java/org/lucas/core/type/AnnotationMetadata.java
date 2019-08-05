package org.lucas.core.type;

import java.util.Set;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {

    Set<String> getAnnotationTypes();

    Set<String> getMetaAnnotationTypes(String annotationName);

    boolean hasAnnotation(String annotationName);

    boolean hasMetaAnnotation(String metaAnnotationName);

    boolean hasAnnotatedMethods(String annotationName);

    Set<MethodMetadata> getAnnotatedMethods(String annotationName);
}
