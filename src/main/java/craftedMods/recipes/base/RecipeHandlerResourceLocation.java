package craftedMods.recipes.base;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.util.ResourceLocation;

/**
 * A resource location with the resource domain of the provider.</br>
 * Normally resources registered by recipe handlers should be registered with this resource
 * location.
 * 
 * @author CraftedMods
 */
public class RecipeHandlerResourceLocation extends ResourceLocation {

	public RecipeHandlerResourceLocation(String path) {
		super(RecipeHandlerUtils.getInstance().getResourceDomain(), path);
	}

}
