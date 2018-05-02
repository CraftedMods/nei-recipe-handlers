/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
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
