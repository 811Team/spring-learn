package org.lucas.core.env;

import org.lucas.core.convert.support.ConfigurableConversionService;
import org.lucas.lang.Nullable;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface ConfigurablePropertyResolver extends PropertyResolver {

    ConfigurableConversionService getConversionService();

    void setConversionService(ConfigurableConversionService conversionService);

    void setPlaceholderPrefix(String placeholderPrefix);

    void setPlaceholderSuffix(String placeholderSuffix);

    void setValueSeparator(@Nullable String valueSeparator);

    void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders);

    void setRequiredProperties(String... requiredProperties);

    void validateRequiredProperties() throws MissingRequiredPropertiesException;
}
