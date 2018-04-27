package craftedMods.recipes.api;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

public interface ResourceHandler {
	
	public Map<ResourceLocation, Supplier<InputStream>> getResources();

}
