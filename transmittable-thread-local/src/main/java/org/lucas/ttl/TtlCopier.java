package org.lucas.ttl;

@FunctionalInterface
public interface TtlCopier<T> {

    T copy(T parentValue);

}
