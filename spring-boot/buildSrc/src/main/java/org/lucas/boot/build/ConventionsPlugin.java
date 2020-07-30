package org.lucas.boot.build;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ConventionsPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        new JavaConventions().apply(project);
        new MavenPublishingConventions().apply(project);
        new AsciidoctorConventions().apply(project);
    }

}
