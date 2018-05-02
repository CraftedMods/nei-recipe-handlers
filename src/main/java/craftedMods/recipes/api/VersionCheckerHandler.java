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
package craftedMods.recipes.api;

import craftedMods.utils.SemanticVersion;

/**
 * A handler which allows version checks.</br>
 * The internal version checker of the provider uses the data supplied by this handler to display
 * the user a message if a new version of this recipe handler unit (usually it's a good choice to use 
 * one version checker for several logically connected recipe handlers) is available. </br>
 * To be loaded, the handler needs to be annotated with
 * {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface VersionCheckerHandler {

	/**
	 * The localized name of the recipe handler unit of this handler
	 * 
	 * @return The recipe handler unit name
	 */
	public String getLocalizedHandlerName();

	/**
	 * The URL to a file which contains the current version data.</br>
	 * It consists of up to three columns separated by a '|'. The first column contains a semantic
	 * version string of the current available version. The second column contains the download URL
	 * of this version and the third the changelog URL. The last two columns are optional.</br>
	 * If null or empty, no version checks will be made.
	 * 
	 * @return A URL to the version file
	 */
	public String getVersionFileURL();

	/**
	 * Returns the current installed version to check against.</br>
	 * If null, no version checks will be made
	 * 
	 * @return The current installed version
	 */
	public SemanticVersion getCurrentVersion();

	/**
	 * A callback which will be invoked after a successful version check.
	 * 
	 * @param remoteVersion
	 *            The version found on the internet
	 */
	public default void onVersionCheck(SemanticVersion remoteVersion) {};

}
