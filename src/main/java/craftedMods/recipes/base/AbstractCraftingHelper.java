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
package craftedMods.recipes.base;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;

public abstract class AbstractCraftingHelper<T extends Recipe> implements RecipeHandlerCraftingHelper<T> {

	@Override
	public boolean matches(ItemStack stack1, ItemStack stack2) {
		return RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(stack1, stack2);
	}

}
