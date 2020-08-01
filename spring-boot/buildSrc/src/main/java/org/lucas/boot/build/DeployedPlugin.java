package org.lucas.boot.build;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlatformPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.bundling.Jar;

public class DeployedPlugin implements Plugin<Project> {


	public static final String GENERATE_POM_TASK_NAME = "generatePomFileForMavenPublication";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(MavenPublishPlugin.class);
		project.getPlugins().apply(MavenRepositoryPlugin.class);
		PublishingExtension publishing = project.getExtensions().getByType(PublishingExtension.class);
		MavenPublication mavenPublication = publishing.getPublications().create("maven", MavenPublication.class);
		project.afterEvaluate((evaluated) -> {
			project.getPlugins().withType(JavaPlugin.class).all((javaPlugin) -> {
				if (((Jar) project.getTasks().getByName(JavaPlugin.JAR_TASK_NAME)).isEnabled()) {
					project.getComponents().matching((component) -> component.getName().equals("java"))
							.all((javaComponent) -> mavenPublication.from(javaComponent));
				}
			});
		});
		project.getPlugins().withType(JavaPlatformPlugin.class)
				.all((javaPlugin) -> project.getComponents()
						.matching((component) -> component.getName().equals("javaPlatform"))
						.all((javaComponent) -> mavenPublication.from(javaComponent)));
	}

}
