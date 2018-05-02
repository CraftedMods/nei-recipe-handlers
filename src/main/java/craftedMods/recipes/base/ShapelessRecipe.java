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

import java.util.Collection;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * A recipe containing the ingredients in no special order.
 * 
 * @author CraftedMods
 */
public class ShapelessRecipe extends AbstractRecipe {

	public ShapelessRecipe(ShapelessOreRecipe recipe) {
		this(recipe.getInput(), recipe.getRecipeOutput());
	}

	public ShapelessRecipe(ShapelessRecipes recipe) {
		this(recipe.recipeItems, recipe.getRecipeOutput());
	}

	public ShapelessRecipe(Collection<?> ingredients, ItemStack result) {
		for (Object ingredient : ingredients)
			if (ingredient != null) {
				this.ingredients.add(this.createItemStackSet(RecipeHandlerUtils.getInstance().extractRecipeItems(ingredient)));
			}
		this.add(result, this.results);
	}

	public ShapelessRecipe(Object ingredient, ItemStack result) {
		this.addAll(RecipeHandlerUtils.getInstance().extractRecipeItems(ingredient), this.ingredients);
		this.add(result, this.results);
	}

}
