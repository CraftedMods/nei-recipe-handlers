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
package craftedMods.recipes.api.utils;

import java.util.Map;

import craftedMods.recipes.utils.ItemStackMapImpl;
import net.minecraft.item.ItemStack;

/**
 * ItemStack hasn't it's own equals and hashCode implementation which means that it won't work well with maps. An ItemStackMap is a map where
 * ItemStacks can be used as keys - just like with normal maps. But instead of a "normal" map this map will work well with ItemStacks.
 * 
 * @author CraftedMods
 * @param <V> The value type of the map
 */
public interface ItemStackMap<V> extends Map<ItemStack, V> {

	/**
	 * Creates a NBT insensitive item stack map
	 * 
	 * @return The item stack map
	 */
	public static <T> ItemStackMap<T> create() {
		return new ItemStackMapImpl<>();
	}

	/**
	 * Creates an item stack map which can be configured to be NBT sensitive
	 * 
	 * @param isNBTSensitive If true, the returned map will be NBT sensitive, if false, not
	 * @return The item stack map
	 */
	public static <T> ItemStackMap<T> create(boolean isNBTSensitive) {
		return new ItemStackMapImpl<>(isNBTSensitive);
	}

}
