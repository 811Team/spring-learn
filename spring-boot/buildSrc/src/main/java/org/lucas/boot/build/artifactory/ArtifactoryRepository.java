package org.lucas.boot.build.artifactory;

import org.gradle.api.Project;

public final class ArtifactoryRepository {

	private final String name;

	private ArtifactoryRepository(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static ArtifactoryRepository forProject(Project project) {
		return new ArtifactoryRepository(determineArtifactoryRepo(project));
	}

	private static String determineArtifactoryRepo(Project project) {
		String version = project.getVersion().toString();
		int modifierIndex = version.lastIndexOf('-');
		if (modifierIndex == -1) {
			return "release";
		}
		String type = version.substring(modifierIndex + 1);
		if (type.startsWith("M") || type.startsWith("RC")) {
			return "milestone";
		}
		return "snapshot";
	}

}
