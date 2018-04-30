package craftedMods.recipes.utils;

import java.net.URL;

import craftedMods.utils.SemanticVersion;

public class RemoteVersion {

	private final SemanticVersion remoteVersion;
	private final URL changelogURL;
	private final URL downloadURL;

	public RemoteVersion(SemanticVersion remoteVersion, URL downloadURL, URL changelogURL) {
		this.remoteVersion = remoteVersion;
		this.downloadURL = downloadURL;
		this.changelogURL = changelogURL;
	}

	public SemanticVersion getRemoteVersion() {
		return this.remoteVersion;
	}

	public URL getDownloadURL() {
		return this.downloadURL;
	}
	
	public URL getChangelogURL() {
		return this.changelogURL;
	}

}
