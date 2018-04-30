package craftedMods.recipes.provider;

import codechicken.nei.api.IConfigureNEI;
import craftedMods.recipes.NEIRecipeHandlers;
import net.minecraft.util.StatCollector;

public class NEIRecipeHandlersAddonConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return StatCollector.translateToLocal("neiRecipeHandlers.integration.name");
	}

	@Override
	public String getVersion() {
		return NEIRecipeHandlers.SEMANTIC_VERSION.toString();
	}

	@Override
	public void loadConfig() {
		NEIRecipeHandlers.mod.getNEIIntegrationManager().load();
	}

}
