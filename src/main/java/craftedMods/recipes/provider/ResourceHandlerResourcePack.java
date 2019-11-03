/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
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
package craftedMods.recipes.provider;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.ResourceHandler;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.*;
import net.minecraft.util.ResourceLocation;

public class ResourceHandlerResourcePack implements IResourcePack {

	private final Map<ResourceLocation, Supplier<InputStream>> resources = new HashMap<>();
	private final Map<ResourceLocation, Collection<Supplier<InputStream>>> langFileParts = new HashMap<>();

	public ResourceHandlerResourcePack(Collection<ResourceHandler> handlers) {
		for (ResourceHandler handler : handlers) {
			Map<ResourceLocation, Supplier<InputStream>> resources = handler.getResources();

			if (resources != null) {
				for (ResourceLocation location : resources.keySet()) {
					if (this.resources.containsKey(location)) {

						// Mark streams for merging - only .lang files are supported currently
						if (location.getResourcePath().endsWith(".lang")) {
							if (!langFileParts.containsKey(location)) {
								langFileParts.put(location, new ArrayList<>());
								langFileParts.get(location).add(this.resources.get(location));
							}
							langFileParts.get(location).add(resources.get(location));
						} else {
							NEIRecipeHandlers.mod.getLogger().warn("The resource " + location.toString() + " was overridden by another resource handler");
						}
					} else {
						this.resources.put(location, resources.get(location));
					}
				}

				// Insert the merged stream
				for (ResourceLocation location : langFileParts.keySet()) {
					Collection<Supplier<InputStream>> parts = langFileParts.get(location);
					Collection<Supplier<InputStream>> partsWithNewLineBetweenStreams = new ArrayList<>();

					// Insert a new line separator after every stream
					for (Supplier<InputStream> part : parts) {
						partsWithNewLineBetweenStreams.add(part);
						partsWithNewLineBetweenStreams.add(() -> {
							return new ByteArrayInputStream("\n".getBytes());
						});
					}

					Supplier<InputStream> sequencedSupplier = () -> {
						return new SequenceInputStream(
								Collections.enumeration(partsWithNewLineBetweenStreams.stream().map(Supplier::get).collect(Collectors.toList())));
					};

					this.resources.put(location, sequencedSupplier);
				}
			}
		}
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
