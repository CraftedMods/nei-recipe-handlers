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
package craftedMods.recipes.api;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

/**
 * A handler which injects resources into the provider.</br>
 * As most recipe handlers shouldn't be their own mod, this is necessary so they can use features
 * which require a registered resource pack (to example localized strings with StatCollector). </br>
 * To be loaded, the handler needs to be annotated with
 * {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface ResourceHandler {

	/**
	 * The resources contained in the map will be injected into the resource pack of the provider
	 * with the provided key (the ResourceLocation).</br>
	 * The resource will be loaded via the InputStream. It's advised that the stream will be created
	 * lazily (when to Supplier is called).
	 * 
	 * @return The resources to inject
	 */
	public Map<ResourceLocation, Supplier<InputStream>> getResources();

}
