package craftedMods.recipes.provider;

import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.*;
import craftedMods.utils.SemanticVersion;

@RegisteredHandler
public class NEIRecipeHandlersVersionCheckerHandler implements VersionCheckerHandler {

	@Override
	public String getLocalizedHandlerName() {
		return NEIRecipeHandlers.MODNAME;
	}

	@Override
	public String getVersionFileURL() {
		return "https://raw.githubusercontent.com/CraftedMods/nei-recipe-handlers/master/version.txt";
	}

	@Override
	public SemanticVersion getCurrentVersion() {
		return NEIRecipeHandlers.SEMANTIC_VERSION;
	}

}
