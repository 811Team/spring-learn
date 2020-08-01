package org.lucas.boot.build.bom.bomr;

import org.lucas.boot.build.bom.Library;
import org.lucas.boot.build.bom.bomr.version.DependencyVersion;

/**
 * An upgrade to change a {@link Library} to use a new version}.
 *
 * @author Andy Wilkinson
 */
final class Upgrade {

	private final Library library;

	private final DependencyVersion version;

	Upgrade(Library library, DependencyVersion version) {
		this.library = library;
		this.version = version;
	}

	Library getLibrary() {
		return this.library;
	}

	DependencyVersion getVersion() {
		return this.version;
	}

}
