package team811.core.env;

import java.util.AbstractCollection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @create: 2018-01-29
 * @description:
 */
public class MissingRequiredPropertiesException extends IllegalStateException {

    private final Set<String> missingRequiredProperties = new LinkedHashSet<>();

    /**
     * 添加异常信息
     *
     * @param key 异常信息
     */
    void addMissingRequiredProperty(String key) {
        this.missingRequiredProperties.add(key);
    }

    /**
     * 获取异常信息
     */
    @Override
    public String getMessage() {
        return "The following properties were declared as required but could not be resolved: " +
                getMissingRequiredProperties();
    }

    /**
     * @see AbstractCollection#toString()
     */
    public Set<String> getMissingRequiredProperties() {
        return this.missingRequiredProperties;
    }
}
