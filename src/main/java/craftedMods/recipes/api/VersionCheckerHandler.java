package craftedMods.recipes.api;

import craftedMods.utils.SemanticVersion;

public interface VersionCheckerHandler {

	public String getLocalizedHandlerName();

	public String getVersionFileURL();// If null or empty, no version checks will be made

	public SemanticVersion getCurrentVersion();// If null, no version checks will be made

	public default void onVersionCheck(SemanticVersion remoteVersion) {};// A callback if the version check succeeded

}
