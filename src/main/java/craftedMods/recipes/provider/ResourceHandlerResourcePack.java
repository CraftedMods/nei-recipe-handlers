package craftedMods.recipes.provider;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;

import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.ResourceHandler;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.*;
import net.minecraft.util.ResourceLocation;

public class ResourceHandlerResourcePack implements IResourcePack {

	private final Map<ResourceLocation, Supplier<InputStream>> resources = new HashMap<>();

	public ResourceHandlerResourcePack(Collection<ResourceHandler> handlers) {
		for (ResourceHandler handler : handlers) {
			Map<ResourceLocation, Supplier<InputStream>> resources = handler.getResources();
			if (resources != null) {
				this.resources.putAll(resources);
			}
		}
	}

	public void addResources(Map<ResourceLocation, Supplier<InputStream>> resources) {
		this.resources.putAll(resources);
	}

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		return this.resourceExists(location) ? this.resources.get(location).get() : null;
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		return this.resources.containsKey(location);
	}

	@Override
	public Set<?> getResourceDomains() {
		return Collections.singleton(NEIRecipeHandlers.MODID);
	}

	@Override
	public IMetadataSection getPackMetadata(IMetadataSerializer serializer, String p_135058_2_) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return NEIRecipeHandlers.MODNAME + " Resource Handler Resources";
	}

}
