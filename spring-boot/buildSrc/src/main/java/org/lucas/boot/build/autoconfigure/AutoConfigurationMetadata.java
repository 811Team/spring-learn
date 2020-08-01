package org.lucas.boot.build.autoconfigure;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import org.springframework.core.CollectionFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.concurrent.Callable;

public class AutoConfigurationMetadata extends DefaultTask {

	private SourceSet sourceSet;

	private File outputFile;

	public AutoConfigurationMetadata() {
		getInputs().file((Callable<File>) () -> new File(this.sourceSet.getOutput().getResourcesDir(),
				"META-INF/spring.factories"));
		dependsOn((Callable<String>) () -> this.sourceSet.getProcessResourcesTaskName());
		getProject().getConfigurations()
				.maybeCreate(AutoConfigurationPlugin.AUTO_CONFIGURATION_METADATA_CONFIGURATION_NAME);
	}

	public void setSourceSet(SourceSet sourceSet) {
		this.sourceSet = sourceSet;
	}

	@OutputFile
	public File getOutputFile() {
		return this.outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	@TaskAction
	void documentAutoConfiguration() throws IOException {
		Properties autoConfiguration = readAutoConfiguration();
		getOutputFile().getParentFile().mkdirs();
		try (FileWriter writer = new FileWriter(getOutputFile())) {
			autoConfiguration.store(writer, null);
		}
	}

	private Properties readAutoConfiguration() throws IOException {
		Properties autoConfiguration = CollectionFactory.createSortedProperties(true);
		Properties springFactories = readSpringFactories(
				new File(this.sourceSet.getOutput().getResourcesDir(), "META-INF/spring.factories"));
		autoConfiguration.setProperty("autoConfigurationClassNames",
				springFactories.getProperty("org.springframework.boot.autoconfigure.EnableAutoConfiguration"));
		autoConfiguration.setProperty("module", getProject().getName());
		return autoConfiguration;
	}

	private Properties readSpringFactories(File file) throws IOException {
		Properties springFactories = new Properties();
		try (Reader in = new FileReader(file)) {
			springFactories.load(in);
		}
		return springFactories;
	}

}
