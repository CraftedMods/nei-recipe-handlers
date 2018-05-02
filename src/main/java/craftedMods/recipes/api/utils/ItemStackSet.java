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

import java.util.*;

import craftedMods.recipes.utils.ItemStackSetImpl;
import net.minecraft.item.ItemStack;

/**
 * ItemStack hasn't an equals and hashCode implementation which means that it won't work well with
 * sets. A ItemStackSet is a set where ItemStacks can be used - just like with a normal set. But
 * instead of normal sets this will work well with ItemStacks.
 * 
 * @author CraftedMods
 * @param <V>
 *            The value type of the map
 */
public interface ItemStackSet extends Set<ItemStack> {

	/**
	 * Returns an NBT insensitive item stack set containing the provided stacks
	 * 
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(ItemStack... stacks) {
		return new ItemStackSetImpl(stacks);
	}

	/**
	 * Returns an item stack set containing the provided stacks
	 * 
	 * @param isNBTSensitive
	 *            If true, the set will be NBT sensitive, if false, not
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(boolean isNBTSensitive, ItemStack... stacks) {
		return new ItemStackSetImpl(isNBTSensitive, stacks);
	}

	/**
	 * Returns an NBT insensitive item stack set containing the provided stacks
	 * 
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(Collection<? extends ItemStack> stacks) {
		return new ItemStackSetImpl(stacks);
	}

	/**
	 * Returns an item stack set containing the provided stacks
	 * 
	 * @param isNBTSensitive
	 *            If true, the set will be NBT sensitive, if false, not
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(boolean isNBTSensitive, Collection<? extends ItemStack> stacks) {
		return new ItemStackSetImpl(isNBTSensitive, stacks);
	}
}
