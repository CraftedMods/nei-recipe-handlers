package craftedMods.recipes.base;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

/**
 * A resource loader gets the input streams for the provided resource locations.</br>
 * It does the "real" resource loading.
 * 
 * @author CraftedMods
 */
public interface RecipeHandlerResourceLoader {

	/**
	 * Registers a resource which has to be loaded.
	 * 
	 * @param location
	 *            The location of the resource
	 * @return Whether the resource could be registered
	 */
	public boolean registerResource(ResourceLocation location);

	/**
	 * Loads the resources from the environment and assigns them to the resource locations under
	 * which they were registered.
	 * 
	 * @return The loaded resources
	 */
	public Map<ResourceLocation, Supplier<InputStream>> loadResources();

}
