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

import codechicken.core.ReflectionManager;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * A recipe containing the ingredients in a special order.
 * 
 * @author CraftedMods
 */
public class ShapedRecipe extends AbstractRecipe {

	private final int width;
	private final int height;

	public ShapedRecipe(ShapedOreRecipe recipe) throws Exception {
		this(ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4).intValue(),
				ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5).intValue(), recipe.getInput(), recipe.getRecipeOutput());
	}

	public ShapedRecipe(ShapedRecipes recipe) {
		this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
	}

	public ShapedRecipe(int width, int height, Object[] ingredients, ItemStack result) {
		this.width = width;
		this.height = height;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Object ingred = ingredients[y * width + x];
				this.ingredients.add(ingred != null ? this.createItemStackSet(RecipeHandlerUtils.getInstance().extractRecipeItems(ingred)) : null);
			}
		}
		this.results.add(this.createItemStackSet(result));
	}

	/**
	 * @return The recipe width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return The recipe height
	 */
	public int getHeight() {
		return this.height;
	}

}
