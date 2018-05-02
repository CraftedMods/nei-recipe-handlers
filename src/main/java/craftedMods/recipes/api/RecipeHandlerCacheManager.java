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
package craftedMods.recipes.api;

import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The cache manager allows the user to store recipes which were computed before.</br>
 * They can be loaded later. This is especially useful if recipe computations are expensive - the
 * recipes will only be computed if they have to. Saving and reusing them can save a lot of time on
 * startup. The provider will automatically recreate the cache if the saved data were deprecated or
 * invalid.
 * 
 * @author CraftedMods
 * @param <T>
 *            The recipe type handled by the cache manager
 */
public interface RecipeHandlerCacheManager<T extends Recipe> {

	public boolean isCacheEnabled();

	/**
	 * Returns whether the cache is valid. Must return false if invalidateCache was called and true
	 * if validateCache was called directly before. If getVersion is not equals to the cache manager version stored
	 * in the cache header, it must return false. Valid means that the data stored there can be used and
	 * that the cache don't has to be recreated.
	 * 
	 * @param cacheHeaderTag
	 *            A tag containing the cache header data
	 * @return Whether the cache is valid
	 */
	public boolean isCacheValid(NBTTagCompound cacheHeaderTag);

	/**
	 * Invalidates the cache which means that it has to be recreated
	 */
	public void invalidateCache();

	/**
	 * Validates the cache which means that it don't has to be recreated
	 */
	public void validateCache();

	/**
	 * A callback invoked if the data should be read from the cache. </br>
	 * Overwrite it to read data from it. The cache content tag contains the recipe data, the header
	 * metadata. The discovered recipes will be returned.
	 * 
	 * @param cacheHeaderTag
	 *            The cache header data
	 * @param cacheContentTag
	 *            The cache content data
	 * @return A collection containing the loaded recipes
	 */
	public Collection<T> readRecipesFromCache(NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag);

	/**
	 * A callback invoked if the data should be written to the cache. </br>
	 * Overwrite it to write data to it. The recipe data should be written to the cache content tag;
	 * only metadata belong to the header.</br>
	 * The cache will be validated after this method was called.
	 * 
	 * @param cacheHeaderTag
	 *            The cache header data
	 * @param cacheContentTag
	 *            The cache content data
	 */
	public void writeRecipesToCache(NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag);

	/**
	 * Returns the cache manager version.</br>
	 * Change this if the cache structure etc. changed to indicate that the cache is no longer valid
	 * and has to be recreated.
	 * 
	 * @return The cache manager version
	 */
	public String getVersion();

}
