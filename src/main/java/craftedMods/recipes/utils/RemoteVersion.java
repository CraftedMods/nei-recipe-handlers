/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
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
