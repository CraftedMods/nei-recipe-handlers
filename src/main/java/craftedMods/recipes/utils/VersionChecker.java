package craftedMods.recipes.utils;

import java.io.*;
import java.net.*;

import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.utils.SemanticVersion;

public class VersionChecker {

	private String versionFileURL;

	private SemanticVersion localVersion = null;
	private RemoteVersion remoteVersion = null;

	public VersionChecker(String versionFileURL, SemanticVersion localVersion) throws MalformedURLException {
		this.localVersion = localVersion;
		this.versionFileURL = versionFileURL;
	}

	public boolean checkVersion() {
		if (this.ping()) {
			try {
				this.remoteVersion = this.parseVersionFile(this.downloadVersionFile());
			} catch (IOException e) {
				NEIRecipeHandlers.mod.getLogger().error(String.format("Couldn't download the version file \"%s\"", this.versionFileURL.toString()), e);
			} catch (Exception e) {
				NEIRecipeHandlers.mod.getLogger().error(String.format("Couldn't parse the contents of the version file \"%s\"", this.versionFileURL.toString()),
						e);
			}
		}
		return this.isNewVersionAvailable();
	}

	private boolean ping() {
		if (this.versionFileURL != null) {
			try {
				URLConnection conn = new URL(this.versionFileURL).openConnection();
				conn.setConnectTimeout(2000);
				conn.connect();
				return true;
			} catch (MalformedURLException e) {
				NEIRecipeHandlers.mod.getLogger().error(String.format("The URL of the version file \"%s\" isn't valid", this.versionFileURL));
			} catch (IOException e) {
				NEIRecipeHandlers.mod.getLogger().error(String.format("Cannot connect to the version file \"%s\"", this.versionFileURL.toString()), e);
			}
		}
		return false;
	}

	private String downloadVersionFile() throws IOException {
		InputStream stream = new URL(this.versionFileURL).openStream();

		String tmp = new String();
		String tmp2 = new String();
		byte[] buffer = new byte[1024];

		while (stream.read(buffer) != -1) {
			tmp = new String(buffer);
			buffer = new byte[1024];
			tmp2 = tmp2.concat(tmp);
		}
		tmp2.trim();
		return tmp2;
	}

	private RemoteVersion parseVersionFile(String versionString) throws MalformedURLException {
		if (versionString != null) {

			SemanticVersion remoteVersion = null;
			URL downloadURL = null;
			URL changelogURL = null;

			String[] parts = versionString.split("\\|");

			remoteVersion = SemanticVersion.of(parts[0]);
			if (parts.length >= 1 && !parts[1].trim().isEmpty()) {
				downloadURL = new URL(parts[1]);
			}
			if (parts.length >= 2 && !parts[2].trim().isEmpty()) {
				changelogURL = new URL(parts[2]);
			}

			return new RemoteVersion(remoteVersion, downloadURL, changelogURL);
		}
		return null;
	}

	public RemoteVersion getRemoteVersion() {
		return this.remoteVersion;
	}

	public boolean isNewVersionAvailable() {
		return this.remoteVersion != null && this.localVersion.compareTo(this.remoteVersion.getRemoteVersion()) < 0;
	}

}
