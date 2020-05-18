package org.lucas.context.annotation;

public enum AdviceMode {

    /**
     * JDK proxy-based advice.
     */
    PROXY,

    /**
     * AspectJ weaving-based advice.
     */
    ASPECTJ

}
