package team811.core.env;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {

    void setActiveProfiles(String... profiles);

    void addActiveProfile(String profile);

    void setDefaultProfiles(String... profiles);

    MutablePropertySources getPropertySources();
}
