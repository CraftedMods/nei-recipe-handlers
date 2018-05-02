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
package craftedMods.recipes.utils;

import java.util.*;

import craftedMods.recipes.api.utils.ItemStackSet;
import net.minecraft.item.ItemStack;

public class ItemStackSetImpl extends AbstractSet<ItemStack> implements ItemStackSet {

	private final boolean isNBTSensitive;
	private final ArrayList<ItemStackWrapper> innerList = new ArrayList<>();
	private final ArrayList<ItemStack> stacksList = new ArrayList<>();

	public ItemStackSetImpl(ItemStack... stacks) {
		this(false, stacks);
	}

	public ItemStackSetImpl(boolean isNBTSensitive, ItemStack... stacks) {
		this.isNBTSensitive = isNBTSensitive;
		for (ItemStack stack : stacks) {
			this.add(stack);
		}
	}

	public ItemStackSetImpl(Collection<? extends ItemStack> stacks) {
		this(false, stacks);
	}

	public ItemStackSetImpl(boolean isNBTSensitive, Collection<? extends ItemStack> stacks) {
		this.isNBTSensitive = isNBTSensitive;
		this.addAll(stacks);
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return this.stacksList.iterator();
	}

	@Override
	public int size() {
		return this.innerList.size();
	}

	@Override
	public boolean isEmpty() {
		return this.innerList.isEmpty();
	}

	public boolean contains(ItemStack stack) {
		return this.innerList.contains(new ItemStackWrapper(stack, this.isNBTSensitive));
	}

	@Override
	public boolean add(ItemStack stack) {
		boolean ret = false;
		ItemStackWrapper wrapper = new ItemStackWrapper(stack, this.isNBTSensitive);
		if (!this.innerList.contains(wrapper)) {
			ret = this.innerList.add(wrapper);
			if (ret) {
				this.stacksList.add(stack);
			}
		}
		return ret;
	}

	public boolean remove(ItemStack stack) {
		return this.innerList.remove(new ItemStackWrapper(stack, this.isNBTSensitive)) && this.stacksList.remove(stack);
	}

	@Override
	public void clear() {
		this.innerList.clear();
		this.stacksList.clear();
	}

}
