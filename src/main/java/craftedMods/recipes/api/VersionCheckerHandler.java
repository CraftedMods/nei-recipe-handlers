package craftedMods.recipes.api;

import craftedMods.utils.SemanticVersion;

/**
 * A handler which allows version checks.</br>
 * The internal version checker of the provider uses the data supplied by this handler to display
 * the user a message if a new version of this recipe handler unit is available. </br>
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
	 * If null or empty, no version checks will be made
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
