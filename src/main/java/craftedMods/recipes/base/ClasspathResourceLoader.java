package craftedMods.recipes.base;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

/**
 * A resource loader implementation which loads the resources from the classpath
 * 
 * @author CraftedMods
 */
public class ClasspathResourceLoader implements RecipeHandlerResourceLoader {

	private Set<ResourceLocation> resourceLocations = new HashSet<>();

	@Override
	public boolean registerResource(ResourceLocation location) {
		return this.resourceLocations.add(location);
	}

	@Override
	public Map<ResourceLocation, Supplier<InputStream>> loadResources() {
		Map<ResourceLocation, Supplier<InputStream>> ret = new HashMap<>();
		this.resourceLocations.forEach(location -> {
			if (this.getClass().getResource("/" + location.getResourcePath()) != null) {
				ret.put(location, () -> {
					return this.getClass().getResourceAsStream("/" + location.getResourcePath());
				});
			}
		});
		return ret;
	}

}
