package team811.core.env;

import team811.core.convert.support.ConfigurableConversionService;
import team811.lang.Nullable;

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
