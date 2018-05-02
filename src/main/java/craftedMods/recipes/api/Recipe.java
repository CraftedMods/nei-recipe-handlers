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

import java.util.List;

import craftedMods.recipes.api.utils.ItemStackSet;
import net.minecraft.item.ItemStack;

/**
 * A recipe contains data about the creation of new items. </br>
 * It consists of ingredients - which will be consumed - and results, which will be produced. Also there can be stacks which are neither ingredients
 * nor results - to example the fuel stacks in smelting recipes.
 * 
 * @author CraftedMods
 */
public interface Recipe {

	/**
	 * Returns a list of recipe items with their permutations by the specified role. </br>
	 * The role decides whether the items are results, ingredients or some other stacks of the recipe. The list contains a set for each ingredient.
	 * Each set contains every valid substitute (permutation) for this ingredient - to example many recipes accept several wood types.
	 * 
	 * @param role The recipe item role
	 * @return The matching permutated ingredients
	 */
	public List<ItemStackSet> getRecipeItems(EnumRecipeItemRole role);

	/**
	 * Returns whether the recipe produces the provided item stack.
	 * 
	 * @param result The result to test against
	 * @return If the recipe produces the provided result
	 */
	public boolean produces(ItemStack result);

	/**
	 * Returns whether the recipe contains the provided item stack as an ingredient.
	 * 
	 * @param ingredient The ingredient to test against
	 * @return If the recipe contains the provided ingredient
	 */
	public boolean consumes(ItemStack ingredient);

	/**
	 * Some recipes have a set of permutations for some ingredients. If NEI shows the usage of such an ingredient which the permutation list contains,
	 * the recipe will only display this ingredient from the permutations list. To achieve this, the ingredient the player selected to display the
	 * recipe will be displayed as the matching ingredients. By doing this, data can enter the recipe which are inaccurate, to example other item
	 * damage values. To nevertheless show an accurate ingredient item, this function can change properties of a copy of the ingredient item (to
	 * example adjust damage values) and return it as the replacement item which finally will be used. If null, the ingredient won't be replaced.
	 * 
	 * @param defaultReplacement The default replacement item stack (the stack the player selected)
	 * @return The final replacement item stack
	 */
	public ItemStack getIngredientReplacement(ItemStack defaultReplacement);

	/**
	 * Some recipes have a set of permutations for some results. If NEI shows the crafting recipe of such an result item which the permutation list
	 * contains, the recipe will only display this result from the permutations list. To achieve this, the result the player selected to display the
	 * recipe will be displayed as the result. By doing this, data can enter the recipe which are inaccurate, to example other item damage values. To
	 * nevertheless show an accurate result item, this function can change properties of a copy of the result item (to example adjust damage values)
	 * and return it as the replacement item which finally will be used. If null, the result won't be replaced.
	 * 
	 * @param defaultReplacement The default replacement item stack (the stack the player selected)
	 * @return The final replacement item stack
	 */
	public ItemStack getResultReplacement(ItemStack defaultReplacement);

}
