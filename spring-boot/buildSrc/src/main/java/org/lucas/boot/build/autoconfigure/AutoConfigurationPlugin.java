package org.lucas.boot.build.autoconfigure;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.lucas.boot.build.DeployedPlugin;
import org.lucas.boot.build.context.properties.ConfigurationPropertiesPlugin;

import java.io.File;
import java.util.Collections;
import java.util.concurrent.Callable;

public class AutoConfigurationPlugin implements Plugin<Project> {

	/**
	 * Name of the {@link Configuration} that holds the auto-configuration metadata
	 * artifact.
	 */
	public static final String AUTO_CONFIGURATION_METADATA_CONFIGURATION_NAME = "autoConfigurationMetadata";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(DeployedPlugin.class);
		project.getPlugins().withType(JavaPlugin.class, (javaPlugin) -> {
			project.getPlugins().apply(ConfigurationPropertiesPlugin.class);
			Configuration annotationProcessors = project.getConfigurations()
					.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME);
			annotationProcessors.getDependencies()
					.add(project.getDependencies().project(Collections.singletonMap("path",
							":spring-boot-project:spring-boot-tools:spring-boot-autoconfigure-processor")));
			annotationProcessors.getDependencies()
					.add(project.getDependencies().project(Collections.singletonMap("path",
							":spring-boot-project:spring-boot-tools:spring-boot-configuration-processor")));
			project.getTasks().create("autoConfigurationMetadata", AutoConfigurationMetadata.class, (task) -> {
				task.setSourceSet(project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets()
						.getByName(SourceSet.MAIN_SOURCE_SET_NAME));
				task.setOutputFile(new File(project.getBuildDir(), "auto-configuration-metadata.properties"));
				project.getArtifacts().add(AutoConfigurationPlugin.AUTO_CONFIGURATION_METADATA_CONFIGURATION_NAME,
						project.provider((Callable<File>) task::getOutputFile), (artifact) -> artifact.builtBy(task));
			});
		});
	}

}
