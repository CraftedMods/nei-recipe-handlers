/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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

	public EnumVersionComparison checkVersion() {
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
		return this.compareRemoteVersion();
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
		try (InputStream stream = new URL(versionFileURL).openStream();
				InputStreamReader bridge = new InputStreamReader(stream);
				BufferedReader reader = new BufferedReader(bridge)) {
			return reader.readLine();
		}
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

	public EnumVersionComparison compareRemoteVersion() {
		int comp = this.remoteVersion != null ? this.localVersion.compareTo(this.remoteVersion.getRemoteVersion()) : 0;
		if (comp == 0) return EnumVersionComparison.CURRENT;
		else if (comp < 0) return EnumVersionComparison.NEWER;
		else return EnumVersionComparison.OLDER;
	}

	public enum EnumVersionComparison {
		CURRENT, OLDER, NEWER;

		public String toDisplayString() {
			return this.name().toLowerCase();
		}
	}

}
