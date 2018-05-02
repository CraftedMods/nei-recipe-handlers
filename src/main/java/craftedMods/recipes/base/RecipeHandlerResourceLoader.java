/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
