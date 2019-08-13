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

import java.util.Collection;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;

/**
 * A recipe containing all fuel items as stacks with the role "OTHER".
 * 
 * @author CraftedMods
 */
public class FurnaceRecipe extends ShapelessRecipe {

	public FurnaceRecipe(Collection<ItemStack> ingredients, ItemStack result) {
		super(ingredients, result);
		this.addFuels();
	}

	public FurnaceRecipe(ItemStack ingredient, ItemStack result) {
		super(ingredient, result);
		this.addFuels();
	}

	protected void addFuels() {
		this.others.add(RecipeHandlerUtils.getInstance().getFuels());
	}

}
