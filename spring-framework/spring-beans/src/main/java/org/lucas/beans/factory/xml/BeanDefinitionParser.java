package org.lucas.beans.factory.xml;

import org.lucas.lang.Nullable;
import org.w3c.dom.Element;

public interface BeanDefinitionParser {

    @Nullable
    BeanDefinition parse(Element element, ParserContext parserContext);


}
